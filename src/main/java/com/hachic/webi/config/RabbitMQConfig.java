package com.hachic.webi.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.ConnectionFactory;

@Configuration
public class RabbitMQConfig {

	public static final String REQUEST_QUEUE = "chat_requests";
	public static final String RESPONSE_QUEUE = "chat_responses";

	@Bean
	public CachingConnectionFactory rabbitConnectionFactory(
			@Value("${spring.rabbitmq.host}") String host,
			@Value("${spring.rabbitmq.port}") int port,
			@Value("${spring.rabbitmq.username}") String username,
			@Value("${spring.rabbitmq.password}") String password) throws Exception {

		RabbitConnectionFactoryBean factoryBean = new RabbitConnectionFactoryBean();
		factoryBean.setHost(host);
		factoryBean.setPort(port);
		factoryBean.setUsername(username);
		factoryBean.setPassword(password);

		// SSL/TLS 사용 설정
		factoryBean.setUseSSL(true);

		// 호스트 이름 검증 비활성화
		factoryBean.setEnableHostnameVerification(false);

		factoryBean.afterPropertiesSet();

		ConnectionFactory connectionFactory = factoryBean.getObject();
		return new CachingConnectionFactory(connectionFactory);
	}

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