package com.hachic.webi.application;

import com.hachic.webi.dao.WebpageRepository;
import com.hachic.webi.domain.Webpage;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebpageService {

    private final WebpageRepository webpageRepository;

    public WebpageService(WebpageRepository webpageRepository) {
        this.webpageRepository = webpageRepository;
    }

    public String saveHtml(String html) {

        String uuid = UUID.randomUUID().toString();
        Webpage webpage = new Webpage(uuid, html);
        webpageRepository.save(webpage);
        return uuid;
    }
}
