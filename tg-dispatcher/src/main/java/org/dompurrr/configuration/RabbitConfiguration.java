package org.dompurrr.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.dompurrr.RabbitQueues.*;

@Configuration
public class RabbitConfiguration {
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public Queue tgTextMessageQueue(){
        return new Queue(TG_TEXT_MESSAGE_UPDATE);
    }

    @Bean
    public Queue tgAnswerMessageQueue(){
        return new Queue(TG_ANSWER_MESSAGE);
    }

}