package com.hachic.webi.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Swagger UI 설정을 위한 Configuration 클래스.
 * HTTPS 환경에서 Swagger UI가 정상 작동하도록 서버 URL을 설정합니다.
 */
@Configuration
public class SwaggerConfig {

	/**
	 * OpenAPI 설정을 생성합니다.
	 * 프로덕션 환경에서는 HTTPS URL을, 개발 환경에서는 HTTP URL을 사용합니다.
	 *
	 * @return OpenAPI 설정 객체
	 */
	@Bean
	public OpenAPI customOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("Webi API Documentation")
						.version("1.0.0")
						.description("Webi 서비스 API 문서"))
				.servers(List.of(
						new Server()
								.url("https://api.webi.click")
								.description("Production Server (HTTPS)"),
						new Server()
								.url("http://localhost:8080")
								.description("Local Development Server")
				));
	}
}
