package org.dompurrr.service.impl;

import lombok.extern.log4j.Log4j;
import org.dompurrr.entities.Resident;
import org.dompurrr.entities.enums.UserState;
import org.dompurrr.service.*;
import org.dompurrr.service.enums.ChatCommands;
import org.dompurrr.utils.ErrorTemplates;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Log4j
public class TgProcessingServiceImpl implements TgProcessingService {
    private final MainService mainService;
    private final ProducerService producerService;
    private final ResidentService residentService;
    private final RoomService roomService;

    public TgProcessingServiceImpl(MainService mainService, ProducerService producerService,
                                   ResidentService residentService, RoomService roomService) {
        this.mainService = mainService;
        this.producerService = producerService;
        this.residentService = residentService;
        this.roomService = roomService;
    }

    /**
     * Sort message from telegram depending on users state.
     * @see UserState
     * @param update received message
     */
    @Override
    public void processTgTextMessage(Update update) {
        Resident curUser = residentService.registerOrGetAccount(update.getMessage().getFrom());
        UserState curUserState = curUser.getUserState();
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        log.info("User " + curUser.getResidentId() + " inputted:"+text);
        if (ChatCommands.CANCEL.cmdEquals(text)){
            sendAnswer(mainService.stopOperation(curUser), chatId);
        }
        else switch (curUserState){
            case WAIT_FOR_COMMAND:
                sendAnswer(mainService.proceedCommand(curUser, text), chatId);
                break;
            case SETTING_DUTY:
                /*
                check if room exist
                check duty info
                make new record in database
                set state to WAIT
                */
                break;
            case REGISTERING_ROOM:
                sendAnswer(roomService.registerRoom(curUser, text), chatId);
                break;
            case CHANGING_NAME:
                sendAnswer(residentService.updateName(curUser, text), chatId);
                break;
            case ADDING_USER:
                sendAnswer(roomService.newRoomResident(curUser, text), chatId);
                break;
            case REMOVING_USER:
                sendAnswer(roomService.removeRoomResident(curUser, text), chatId);
                break;
            case JOINS_THE_ROOM:
                sendAnswer(residentService.joinRoom(curUser, text), chatId);
                break;
            default:
                log.error("Got incorrect user "+curUser.getResidentId() + " state " + curUser.getUserState().name());
                sendAnswer(ErrorTemplates.UNDEFINED_PROBLEM, chatId);
                break;
        }
    }

    /**
     * Sends answer to specified chat
     * @param output text to send
     * @param chatId chatId of recipient
     */
    private void sendAnswer(String output, Long chatId) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.produceAnswer(sendMessage);
    }
}
