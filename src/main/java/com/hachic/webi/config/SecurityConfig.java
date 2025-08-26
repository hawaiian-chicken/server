package com.hachic.webi.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorizeRequests -> authorizeRequests
						// 소셜 로그인 엔드포인트와 swagger는 접근 허용
						.requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/docs/**").permitAll()
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

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("https://api.webi.click"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
