package com.hachic.webi.process.application;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.hachic.webi.config.RabbitMQConfig;
import com.hachic.webi.process.dto.ProcessRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {

	private final ProcessService processService;

	/**
	 * 'chat_requests' 큐를 구독한다.
	 * Node.js 서버에서 메시지를 보내면 이 메소드가 자동으로 실행된다.
	 * @param request Node.js로부터 받은 요청 데이터
	 */
	@RabbitListener(queues = RabbitMQConfig.REQUEST_QUEUE)
	public void receiveRequest(ProcessRequest request) {
		log.info("Received message via RabbitMQ for user: {}", request.userId());
		try {
			processService.processHtml(request);
		} catch (IOException e) {
			log.error("Error processing message for user: {}. Details: {}", request.userId(), e.getMessage());
			// TODO: 에러 발생 시 처리 로직
		}
	}
}