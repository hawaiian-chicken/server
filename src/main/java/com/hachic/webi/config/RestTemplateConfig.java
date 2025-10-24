package com.hachic.webi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate을 스프링 빈으로 등록하는 설정 클래스입니다
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

		// 연결 타임아웃 (10분)
		requestFactory.setConnectTimeout(600000);

		// 응답 대기 타임아웃 (10분)
		requestFactory.setReadTimeout(600000);

		return new RestTemplate(requestFactory);
	}
}
