package com.hachic.webi.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String REQUEST_QUEUE = "chat_requests";
	public static final String RESPONSE_QUEUE = "chat_responses";

	@Bean
	public Queue requestQueue() {
		return new Queue(REQUEST_QUEUE, true);
	}

	@Bean
	public Queue responseQueue() {
		return new Queue(RESPONSE_QUEUE, true);
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
