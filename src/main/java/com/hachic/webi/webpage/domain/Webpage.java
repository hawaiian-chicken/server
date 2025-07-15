package com.hachic.webi.webpage.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * 웹 페이지 도메인 엔티티
 */
@Getter
@Document(collection = "webpages")
public class Webpage {

    @Id
    private String id;

    private String userId;

    private String html;

    private long createdAt;

    /**
     * 웹 페이지 생성자
     *
     * @param userId user_id
     * @param html HTML 내용
     */
    public Webpage(String userId, String html) {
        this.userId = userId;
        this.html = html;
        this.createdAt = Instant.now().getEpochSecond();
    }
}
