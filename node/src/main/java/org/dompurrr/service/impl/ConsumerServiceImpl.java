package org.dompurrr.service.impl;

import lombok.extern.log4j.Log4j;
import org.dompurrr.service.ConsumerService;
import org.dompurrr.service.TgProcessingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Component
public class ConsumerServiceImpl implements ConsumerService {
    private final TgProcessingService tgProcessingService;

    public ConsumerServiceImpl(TgProcessingService tgProcessingService) {
        this.tgProcessingService = tgProcessingService;
    }

    @Override
    @RabbitListener(queues = "telegram_text_result")
    public void consumeTgTextMessage(Update update) {
        log.debug("NODE: New message is received");
        tgProcessingService.processTgTextMessage(update);
    }
}
