package com.hachic.webi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * MongoDB Auditing을 활성화하는 Configuration 클래스입니다.
 * @CreatedDate 등의 필드를 자동으로 주입할 수 있도록 합니다.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
}
