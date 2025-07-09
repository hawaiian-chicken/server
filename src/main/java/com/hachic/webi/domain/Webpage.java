package com.hachic.webi.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "webpages")
public class Webpage {

    @Id
    private String id;

    @Getter
    private String uuid;

    @Getter
    private String html;

    @Getter
    private long createdAt;

    public Webpage(String uuid, String html) {

        this.uuid = uuid;
        this.html = html;
        this.createdAt = Instant.now().getEpochSecond();
    }

}
