package com.rebellion.notifyhub.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.rebellion.notifyhub.config.RabbitMQConfig;
import com.rebellion.notifyhub.entity.NotificationEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(NotificationEvent event){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EVENTS_EXCHANGE, RabbitMQConfig.EVENTS_ROUNTING_KEY, event);
    }
}
