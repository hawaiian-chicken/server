package com.hachic.webi.webpage.application;

import com.hachic.webi.webpage.dao.WebpageRepository;
import com.hachic.webi.webpage.domain.Webpage;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
     * HTML을 저장하고 userId를 반환한다.
     *
     * @param html HTML 내용
     * @return userId
     */
    public String saveHtml(String html, String userId) {

        Optional<Webpage> existWebpage = webpageRepository.findByUserId(userId);

        Webpage webpage = new Webpage(userId, html);
        webpageRepository.save(webpage);

        return userId;
    }
}
