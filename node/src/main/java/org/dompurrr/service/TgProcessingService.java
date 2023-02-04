package org.dompurrr.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TgProcessingService {
    void processTgTextMessage(Update update);
}
