package com.hachic.webi.webpage.dao;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.hachic.webi.webpage.domain.Webpage;

/**
 * 웹 페이지 레포지토리
 */
public interface WebpageRepository extends MongoRepository<Webpage, ObjectId> {

	/**
	 * userId로 웹페이지를 조회한다.
	 *
	 * @param userId user_id
	 * @return 웹 페이지 Optional
	 */
	Optional<Webpage> findByUserId(String userId);
}
