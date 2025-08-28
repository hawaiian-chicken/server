package com.hachic.webi.webpage.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hachic.webi.webpage.dao.WebpageRepository;
import com.hachic.webi.webpage.domain.Webpage;

/**
 * 웹 페이지 서비스
 */
@Service
public class WebpageService {

	private final WebpageRepository webpageRepository;

	/**
	 * 웹 페이지 서비스 생성자
	 *
	 * @param webpageRepository 웹페이지 레포지토리
	 */
	public WebpageService(WebpageRepository webpageRepository) {
		this.webpageRepository = webpageRepository;
	}

	/**
	 * HTML을 저장하고 Webpage 객체를 반환한다.
	 *
	 * @param html HTML 내용
	 * @param userId 사용자 ID
	 * @return 저장된 웹 페이지 객체
	 */
	public Webpage saveHtml(String html, String userId) {

		Optional<Webpage> existWebpage = webpageRepository.findByUserId(userId);

		Webpage webpage = new Webpage(userId, html);

		return webpageRepository.save(webpage);
	}
}

