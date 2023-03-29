package org.dompurrr.service.impl;

import lombok.extern.log4j.Log4j;
import org.dompurrr.dao.ResidentDAO;
import org.dompurrr.dao.RoomDAO;
import org.dompurrr.entities.Resident;
import org.dompurrr.entities.Room;
import org.dompurrr.entities.enums.AccountStatus;
import org.dompurrr.entities.enums.RoomStatus;
import org.dompurrr.entities.enums.UserState;
import org.dompurrr.service.RoomService;
import org.dompurrr.utils.ErrorTemplates;
import org.dompurrr.utils.TextUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
public class RoomServiceImpl implements RoomService {
    private final TextUtils textUtils;
    private final RoomDAO roomDAO;
    private final ResidentDAO residentDAO;

    public RoomServiceImpl(TextUtils textUtils, RoomDAO roomDAO, ResidentDAO residentDAO) {
        this.textUtils = textUtils;
        this.roomDAO = roomDAO;
        this.residentDAO = residentDAO;
    }

    @Override
    public String registerRoom(Resident resident, String roomName) {
        if (resident == null){
            log.error("Received null resident in registerRoom");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
        if (textUtils.matchRoomName(roomName)){
            Room newRoom = createRoom(roomName);
            resident.setRoom(newRoom);
            resident.setUserState(UserState.WAIT_FOR_COMMAND);
            resident.setStatus(AccountStatus.ONLY_TG);
            residentDAO.save(resident);
            log.info("Successful room " + newRoom.getRoomId() + " creation, creator " + resident.getResidentId());
            return getRoomInfo(newRoom);
        }
        else{
            log.warn("Unsuccessful room creation (bad name)");
            return ErrorTemplates.BAD_ROOM_NAME;
        }
    }

    private Room createRoom(String roomName){
        Room roomToCreate = new Room();
        roomToCreate.setRoomName(roomName);
        roomToCreate.setStatus(RoomStatus.NO_CHAT);
        roomDAO.save(roomToCreate);
        return roomToCreate;
    }

    @Override
    public String getRoomInfo(Room room) {
        if (room == null){
            log.error("Provided null room");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
        Room updatedRoom = roomDAO.findByRoomId(room.getRoomId());
        if (updatedRoom == null){
            log.error("Provided room removed from database");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
        List<Resident> resList = updatedRoom.getResidentList();
        StringBuilder res = new StringBuilder("Название комнаты: " + updatedRoom.getRoomName() + "\n");
        res.append("Количество участников: ").append(resList.size()).append("\n");
        for (Resident curRes: resList)
            res.append("    Участник №").append(curRes.getResidentId()).append(": ").append(curRes.getResidentName()).append("\n");
        res.append("Токен комнаты для подключения: ").append(hasToken(updatedRoom)?updatedRoom.getToken():"Отсутствует").append("\n");
        return res.toString();
    }

    @Override
    public String newRoomResident(Resident resident, String residentName) {
        if (resident.getRoom() == null){
            log.warn("Resident without room tried to add user");
            return ErrorTemplates.NO_ROOM;
        }
        if (textUtils.matchName(residentName)) {
            resident.setUserState(UserState.WAIT_FOR_COMMAND);
            residentDAO.save(resident);
            return addUser(resident.getRoom(), residentName);
        }
        else{
            return ErrorTemplates.BAD_NAME;
        }
    }

    private String addUser(Room room, String userName) {
        if (room == null){
            log.error("Provided null room");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
        Resident newResident = Resident.builder()
                .residentName(userName)
                .status(AccountStatus.NOT_REGISTERED)
                .room(room)
                .build();
        residentDAO.save(newResident);
        log.info("New user "+ newResident.getResidentId() +" was added to room " + room.getRoomId());
        return ("Пользователь " + newResident.getResidentName() + " добавлен в комнату!");
    }

    public String removeRoomResident(Resident resident, String userId){
        if (resident == null){
            log.error("Provided null resident");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
        if (textUtils.matchNum(userId)){
            Room room = resident.getRoom();
            return removeUser(room, userId, resident);
        }
        else{
            return ErrorTemplates.BAD_NUMBER;
        }
    }

    private String removeUser(Room room, String id, Resident resident) {
        Resident residentToRemove = residentDAO.findByResidentId(Long.parseLong(id));
        if (residentToRemove == null) {
            log.warn("Unable to find resident " + id);
            return ErrorTemplates.RESIDENT_NOT_FOUND;
        }
        if (residentToRemove.getRoom().getRoomId().equals(room.getRoomId())) {
            residentToRemove.setRoom(null);
            resident.setUserState(UserState.WAIT_FOR_COMMAND);
            residentDAO.save(residentToRemove);
            residentDAO.save(resident);
            log.info("Resident " + residentToRemove.getResidentId() + "was removed from "
                    + room.getRoomId() + " by " + resident.getResidentId());
            return ("Пользователь " + residentToRemove.getResidentName() + " был удален из комнаты.");
        }
        else
            return ErrorTemplates.RESIDENT_NOT_IN_ROOM;
    }

    public String residentList(Room room) {
        if (room == null){
            log.error("Provided null room");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
        Room updatedRoom = roomDAO.findRoomWithResidentsByRoomId(room.getRoomId());
        if (updatedRoom == null){
            log.error("Provided room isn't find in table, but it should");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
        StringBuilder ans = new StringBuilder();
        for (Resident tmp: updatedRoom.getResidentList()){
            ans.append(tmp.getResidentId()).append(": ").append(tmp.getResidentName()).append("\n");
        }
        return ans.toString();
    }

    @Override
    public String createRoomToken(Room room) {
        if (room.getToken() == null){
            String generatedToken = textUtils.makeToken();
            room.setToken(generatedToken);
            roomDAO.save(room);
            log.info("In room " + room.getRoomId() + " was created invite token");
            return generatedToken;
        }
        else{
            log.error("Provided room with token");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
    }

    @Override
    public boolean hasVacant(Room room) {
        List<Resident> residents = room.getResidentList();
        for (Resident dude: residents){
            if (dude.getStatus().equals(AccountStatus.NOT_REGISTERED)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void dropVacant(Room room) {
        List<Resident> residents = room.getResidentList();
        for (Resident dude: residents){
            if (dude.getStatus().equals(AccountStatus.NOT_REGISTERED)){
                dude.setRoom(null);
                residentDAO.save(dude);
                return;
            }
        }
        log.error("Attempt to remove vacant from non vacant room");
    }

    @Override
    public boolean hasToken(Room room) {
        return room.getToken() != null;
    }
}
