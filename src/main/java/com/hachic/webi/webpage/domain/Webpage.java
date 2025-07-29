package com.hachic.webi.webpage.domain;

import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * 웹 페이지 도메인 엔티티
 */
@Getter
@Document(collection = "webpages")
public class Webpage {

    @Id
    private ObjectId id;

    private String userId;

    private String html;

    private LocalDateTime createdAt;

    /**
     * MongoDB용 기본 생성자
     */
    protected Webpage() {}

    /**
     * 웹 페이지 생성자
     *
     * @param userId user_id 사용자 식별자로 현재는 hawaii로 임시 설정됨
     * @param html HTML 내용
     */
    public Webpage(String userId, String html) {
        this.userId = userId;
        this.html = html;
        this.createdAt = LocalDateTime.now();
    }
}
