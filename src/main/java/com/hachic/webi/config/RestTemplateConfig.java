package com.hachic.webi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate을 스프링 빈으로 등록하는 설정 클래스입니다.
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
