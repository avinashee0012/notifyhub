package com.rebellion.notifyhub.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rebellion.notifyhub.entity.NotificationEvent;
import com.rebellion.notifyhub.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "notifyhub.events.queue")
    public void consume(NotificationEvent event) {
		System.out.println(event);
        notificationService.createNotification(event);
    }
}
