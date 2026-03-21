package com.rebellion.notifyhub.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String EVENTS_QUEUE = "notifyhub.events.queue";
	public static final String EVENTS_EXCHANGE = "notifyhub.events.exchange";
	public static final String EVENTS_ROUNTING_KEY = "notifyhub.events.routing";

	@Bean
	public Queue eventsQueue() {
		return new Queue(EVENTS_QUEUE);
	}

	@Bean
	public TopicExchange eventsExchange() {
		return new TopicExchange(EVENTS_EXCHANGE);
	}

	@Bean
	public Binding bindings() {
		return BindingBuilder.bind(eventsQueue()).to(eventsExchange()).with(EVENTS_ROUNTING_KEY);
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public Queue notificationQueue() {
		return QueueBuilder.durable("notification.queue").withArgument("x-dead-letter-exchange", "notification.dlx").build();
	}

	@Bean
	public Queue deadLetterQueue() {
		return new Queue("notification.dlq");
	}

	@Bean
	public Binding dlqBinding() {
		return BindingBuilder.bind(deadLetterQueue()).to(new DirectExchange("notification.dlx")).with("dlq");
	}
}
