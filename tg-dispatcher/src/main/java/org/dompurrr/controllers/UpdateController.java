package org.dompurrr.controllers;

import lombok.extern.log4j.Log4j;
import org.dompurrr.service.UpdateProducer;
import org.dompurrr.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;

    private final UpdateProducer updateProducer;

    private final MessageUtils messageUtils;

    public UpdateController(UpdateProducer updateProducer, MessageUtils messageUtils) {
        this.updateProducer = updateProducer;
        this.messageUtils = messageUtils;
    }

    public void registerBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update){
        if (update == null) {
            log.error("Got null Update.");
            return;
        }

        if (update.hasMessage()){
            sortUpdate(update);
        }
        else{
            log.error("Got an empty message.");
        }
    }

    private void sortUpdate(Update update){
        var message = update.getMessage();
        if (message.hasText()){
            produceText(update);
        }
        else{
            log.info("Got message without text.");
            SendMessage response = messageUtils.generateSendMessageWithText(update, "Полученно сообщение без текста. Поддерживается только текст.");
            setView(response);
        }
    }

    private void invalidRequestResponse(Update update){
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Неверный запрос!");
        setView(sendMessage);
    }

    private void produceText(Update update){
        updateProducer.produce("telegram_text_result", update);
    }

    public void setView(SendMessage sendMessage){
        telegramBot.sendAnswerMessage(sendMessage);
    }
}
