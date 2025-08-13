package com.hachic.webi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorizeRequests -> authorizeRequests
						// 소셜 로그인 엔드포인트는 접근 허용
						.requestMatchers(
								"/swagger-ui/index.html",
								"/auth/google",
								"/auth/kakao",
								"/auth/naver",
								"/auth/google/callback",
								"/auth/kakao/callback",
								"/auth/naver/callback").permitAll()
						// 나머지 요청은 인증 요구
						.anyRequest().authenticated()
				)
				.formLogin(formLogin -> formLogin
						// 로그인 페이지는 모든 사용자에게 허용됨
						.loginPage("/login").permitAll()
				)
				// CSRF 보호 비활성화 -> 개발 중에만 사용
				.csrf(AbstractHttpConfigurer::disable);

		return http.build();
	}
}
