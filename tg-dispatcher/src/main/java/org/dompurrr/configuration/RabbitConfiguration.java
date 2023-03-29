package org.dompurrr.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public Queue tgTextMessageQueue(){
        return new Queue("telegram_text_result");
    }

    @Bean
    public Queue tgAnswerMessageQueue(){
        return new Queue("telegram_answer_message");
    }

}