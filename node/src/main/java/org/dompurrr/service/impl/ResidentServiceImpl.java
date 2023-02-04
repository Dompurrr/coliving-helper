package org.dompurrr.service.impl;

import lombok.extern.log4j.Log4j;
import org.dompurrr.dao.ResidentDAO;
import org.dompurrr.dao.RoomDAO;
import org.dompurrr.entities.Resident;
import org.dompurrr.entities.Room;
import org.dompurrr.entities.enums.AccountStatus;
import org.dompurrr.entities.enums.UserState;
import org.dompurrr.service.ResidentService;
import org.dompurrr.service.RoomService;
import org.dompurrr.utils.AnswerTemplates;
import org.dompurrr.utils.ErrorTemplates;
import org.dompurrr.utils.TextUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Log4j
@Service
public class ResidentServiceImpl implements ResidentService {
    private final ResidentDAO residentDAO;
    private final TextUtils textUtils;
    private final RoomDAO roomDAO;

    private final RoomService roomService;

    public ResidentServiceImpl(ResidentDAO residentDAO, TextUtils textUtils, RoomDAO roomDAO, RoomService roomService) {
        this.residentDAO = residentDAO;
        this.textUtils = textUtils;
        this.roomDAO = roomDAO;
        this.roomService = roomService;
    }

    @Override
    public Resident registerOrGetAccount(User sender) {
        Long senderId = sender.getId();
        Resident residentTg = residentDAO.findByTgPageRef(senderId);
        if (residentTg != null){
            log.debug("Successful user tg search: "+ residentTg.getResidentId() + ".");
            return residentTg;
        }
        return registerNew(sender);
    }

    @Override
    public Resident registerNew(User sender) {
        Resident toAdd = Resident.builder()
                .residentName(sender.getFirstName())
                .tgPageRef(sender.getId())
                .status(AccountStatus.NO_ROOM_ONLY_TG)
                .userState(UserState.WAIT_FOR_COMMAND)
                .build();
        return residentDAO.save(toAdd);
    }

    @Override
    public boolean hasToken(Resident resident) {
        if (resident==null){
            log.error("Got null Resident in hasToken");
            return false;
        }
        return (resident.getRefToken()!=null);
    }

    @Override
    public boolean hasRoom(Resident resident) {
        if (resident==null){
            log.error("Got null Resident in hasRoom");
            return false;
        }
        return (resident.getRoom()!=null);
    }

    @Override
    public String updateName(Resident resident, String newName) {
        if (textUtils.matchName(newName)){
            if (resident != null){
                resident.setResidentName(newName);
                resident.setUserState(UserState.WAIT_FOR_COMMAND);
                residentDAO.save(resident);
                log.info("User " + resident.getResidentId() + " changed name to " + newName);
                return ("Имя успешно обновлено, новое имя: " + newName);
            }
            else{
                log.error("Provided null resident");
                return ErrorTemplates.UNDEFINED_PROBLEM;
            }
        }
        else{
            log.warn("Unsuccessful name change, provided name " + newName);
            return ErrorTemplates.BAD_NAME;
        }
    }

    @Override
    public String residentInfo(Resident resident) {
        if (resident==null){
            log.error("Provided null resident");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
        String ans = "Информация о вас:\n";
        ans += "Имя: " + resident.getResidentName() + "\n";
        ans += "Комната: " + (resident.getRoom()!=null?resident.getRoom():"Отсутствует") + "\n";
        ans += "Аккаунт телеграм: " + (resident.getTgPageRef()!=null?resident.getTgPageRef():"Отсутствует") + "\n";
        ans += "Аккаунт вк: " + (resident.getVkPageRef()!=null?resident.getVkPageRef():"Отсутствует") + "\n";
        ans += "Токен профиля: " + (resident.getRefToken()!=null?resident.getRefToken():"Отсутствует, сгенерируйте командой /register");
        return ans;
    }

    @Override
    public String generateToken(Resident resident) {
        if (resident==null){
            log.error("Provided null resident");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
        if (resident.getRefToken() == null) {
            String generatedToken = textUtils.makeToken();
            resident.setRefToken(generatedToken);
            residentDAO.save(resident);
            log.info("User " + resident.getResidentId() + " generated token");
            return generatedToken;
        }
        else{
            return ("У вас уже имеется токен: " + resident.getRefToken());
        }
    }

    @Override
    public String joinRoom(Resident resident, String data) {
        if (resident == null || data == null){
            log.error("Provided null variables");
            return ErrorTemplates.UNDEFINED_PROBLEM;
        }
        String[] destructedData = textUtils.dataDestructor(data);
        if (destructedData != null){
            Room room = roomDAO.findByRoomId(Long.parseLong(destructedData[0]));
            if ((room != null) && (room.getToken().equals(destructedData[1]))){
                if (roomService.hasVacant(room)){
                    roomService.dropVacant(room);
                    resident.setRoom(room);
                    resident.setUserState(UserState.WAIT_FOR_COMMAND);
                    residentDAO.save(resident);
                    return AnswerTemplates.SUCCESS_JOIN;
                }
                else{
                    return ErrorTemplates.ROOM_IS_FULL;
                }
            }
            else{
                return ErrorTemplates.ROOM_NOT_FOUND;
            }
        }
        else{
            log.debug("Invalid data provided by " + resident.getResidentId());
            return ErrorTemplates.BAD_DATA;
        }
    }
}
