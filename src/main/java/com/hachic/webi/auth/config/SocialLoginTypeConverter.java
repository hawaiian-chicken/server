package com.hachic.webi.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import com.hachic.webi.auth.domain.SocialLoginType;

@Configuration
public class SocialLoginTypeConverter implements Converter<String, SocialLoginType> {
	/**
	 * URL 경로의 소문자를 enum 타입(대문자)으로 변환하는 역할
	 * @param source url로 전달된 소문자 파라미터
	 * @return 대문자의 enum 타입
	 */
	@Override
	public SocialLoginType convert(String source) {
		return SocialLoginType.valueOf(source.toUpperCase());
	}
}
