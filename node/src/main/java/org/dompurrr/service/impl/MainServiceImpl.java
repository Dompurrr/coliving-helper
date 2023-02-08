package org.dompurrr.service.impl;

import lombok.extern.log4j.Log4j;
import org.dompurrr.dao.ResidentDAO;
import org.dompurrr.dao.RoomDAO;
import org.dompurrr.entities.Resident;
import org.dompurrr.entities.Room;
import org.dompurrr.entities.enums.UserState;
import org.dompurrr.service.MainService;
import org.dompurrr.service.PurchaseService;
import org.dompurrr.service.ResidentService;
import org.dompurrr.service.RoomService;
import org.dompurrr.service.enums.ChatCommands;
import org.dompurrr.utils.AnswerTemplates;
import org.dompurrr.utils.ErrorTemplates;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Map.entry;

@Log4j
@Component
public class MainServiceImpl  implements MainService {

    private static final Map<String, ChatCommands> commandMap = Map.ofEntries(
            entry("/start", ChatCommands.START),
            entry("/help", ChatCommands.HELP),
            entry("/register", ChatCommands.REGISTRATION),
            entry("/info", ChatCommands.INFO),
            entry("/changeName", ChatCommands.CHANGE_NAME),
            entry("/cancel", ChatCommands.CANCEL),
            entry("/room", ChatCommands.ROOM),
            entry("/addUser", ChatCommands.ADD_ROOM),
            entry("/removeUser", ChatCommands.REMOVE_ROOM),
            entry("/deleteRoom", ChatCommands.DELETE_ROOM),
            entry("/joinRoom", ChatCommands.JOIN_ROOM),
            entry("/inviteToRoom", ChatCommands.INVITE_ROOM),
            entry("/createPurchase", ChatCommands.CREATE_PURCHASE),
            entry("/getPurchases", ChatCommands.GET_PURCHASES)
    );
    private final ResidentDAO residentDAO;
    private final RoomDAO roomDAO;
    private final RoomService roomService;
    private final ResidentService residentService;
    private final PurchaseService purchaseService;

    public MainServiceImpl(ResidentDAO residentDAO, RoomDAO roomDAO, RoomService roomService, ResidentService residentService, PurchaseService purchaseService) {
        this.residentDAO = residentDAO;
        this.roomDAO = roomDAO;
        this.roomService = roomService;
        this.residentService = residentService;
        this.purchaseService = purchaseService;
    }

    @Override
    public String proceedCommand(Resident resident, String command) {
        if (resident==null){
            log.error("Provided null resident");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
        log.info("User "+ resident.getResidentId() +" called " + command);
        ChatCommands input = commandMap.get(command);
        if (input == null){
            log.debug("User " + resident.getResidentId() + " made wrong input " + command);
            return ErrorTemplates.WRONG_INPUT;
        }
        switch (input){
            case START:
                return AnswerTemplates.WELCOME_MESSAGE;
            case CANCEL:
                return stopOperation(resident);
            case ROOM:
                return roomOperation(resident);
            case REGISTRATION:
                return residentService.generateToken(resident);
            case ADD_ROOM:
                return roomAddOperation(resident);
            case CHANGE_NAME:
                return nameChangeOperation(resident);
            case REMOVE_ROOM:
                return roomRemoveOperation(resident);
            case INFO:
                return residentService.residentInfo(resident);
            case HELP:
                return AnswerTemplates.HELP_MESSAGE;
            case INVITE_ROOM:
                return roomInviteOperation(resident);
            case JOIN_ROOM:
                return roomJoinOperation(resident);
            case CREATE_PURCHASE:
                return createPurchaseOperation(resident);
            case GET_PURCHASES:
                return purchaseService.getPurchases(resident.getRoom());
            default:
                log.error("Received unsupported command that is in commandMap");
                break;
        }
        log.error("Command not processed " + resident.getResidentId() + " input " + command);
        return ErrorTemplates.UNDEFINED_PROBLEM;
    }

    @Override
    public String stopOperation(Resident resident) {
        resident.setUserState(UserState.WAIT_FOR_COMMAND);
        residentDAO.save(resident);
        return AnswerTemplates.CANCEL_MESSAGE;
    }

    @Override
    public String roomOperation(Resident resident) {
        if (residentService.hasRoom(resident)){
            return ("Ваша комната:\n" + roomService.getRoomInfo(resident.getRoom()));
        }
        else {
            log.debug("User " + resident.getResidentId() + " started room creation");
            resident.setUserState(UserState.REGISTERING_ROOM);
            residentDAO.save(resident);
            return AnswerTemplates.ROOM_CREATION;
        }
    }

    @Override
    public String roomAddOperation(Resident resident) {
        if (residentService.hasRoom(resident)) {
            Room updatedRoom = roomDAO.findRoomWithResidentsByRoomId(resident.getRoom().getRoomId());
            if (updatedRoom.getResidentList().size()>=20){
                return ErrorTemplates.MAX_RESIDENTS_NUM;
            }
            else {
                log.debug("User " + resident.getResidentId() + " started user add");
                resident.setUserState(UserState.ADDING_USER);
                residentDAO.save(resident);
                return ("Введите ник пользователя для добавления в комнату " + updatedRoom.getRoomName());
            }
        }
        else{
            return ErrorTemplates.NO_ROOM;
        }
    }

    @Override
    public String nameChangeOperation(Resident resident) {
        log.debug("User " + resident.getResidentId() + " started name change");
        resident.setUserState(UserState.CHANGING_NAME);
        residentDAO.save(resident);
        return AnswerTemplates.NAME_CHANGE;
    }

    @Override
    public String roomRemoveOperation(Resident resident) {
        if (resident.getRoom() != null) {
            log.debug("User " + resident.getResidentId() + " started user remove");
            resident.setUserState(UserState.REMOVING_USER);
            residentDAO.save(resident);
            return roomService.residentList(resident.getRoom());
        }
        else {
            return ErrorTemplates.NO_ROOM;
        }
    }

    @Override
    public String roomInviteOperation(Resident resident) {
        if (resident.getRoom() != null){
            if (resident.getRoom().getToken() == null){
                return roomService.createRoomToken(resident.getRoom());
            }
            else{
                return ("Токен комнаты: " + resident.getRoom().getToken());
            }
        }
        else{
            log.debug("User " + resident.getResidentId() + " attempted to invite without room");
            return ErrorTemplates.NO_ROOM;
        }
    }

    @Override
    public String roomJoinOperation(Resident resident) {
        if (resident == null){
            log.debug("Provided null resident");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
        if (resident.getRoom() == null){
            resident.setUserState(UserState.JOINS_THE_ROOM);
            residentDAO.save(resident);
            return AnswerTemplates.ROOM_JOIN;
        }
        else {
            log.debug("User " + resident.getRoom().getRoomId() + " attempted to join with room");
            return ErrorTemplates.HAS_ROOM;
        }
    }

    @Override
    public String createPurchaseOperation(Resident resident) {
        if (resident == null){
            log.debug("Provided null resident");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
        if (resident.getRoom() == null){
            log.debug("Attempt to create purchase without room");
            return ErrorTemplates.NO_ROOM;
        }
        else {
            log.info("User " + resident.getResidentId() + " started purchase creation");
            resident.setUserState(UserState.CREATING_PURCHASE);
            residentDAO.save(resident);
            return AnswerTemplates.PURCHASE_CREATION;
        }
    }
}
