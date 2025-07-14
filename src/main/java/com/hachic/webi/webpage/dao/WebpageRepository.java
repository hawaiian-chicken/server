package com.hachic.webi.webpage.dao;

import com.hachic.webi.webpage.domain.Webpage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * 웹 페이지 레포지토리
 */
public interface WebpageRepository extends MongoRepository<Webpage, String> {

    /**
     * UUID로 웹페이지를 조회한다.
     *
     * @param uuid UUID
     * @return 웹 페이지 Optional
     */
    Optional<Webpage> findByUuid(String uuid);
}
