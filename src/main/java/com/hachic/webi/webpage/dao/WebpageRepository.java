package com.hachic.webi.webpage.dao;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.hachic.webi.webpage.domain.Webpage;
import com.hachic.webi.webpage.dto.HtmlDto;

/**
 * 웹 페이지 레포지토리
 */
public interface WebpageRepository extends MongoRepository<Webpage, String> {

	/**
	 * userId로 웹페이지를 조회한다.
	 *
	 * @param userId user_id
	 * @return 웹 페이지 Optional
	 */
	Optional<Webpage> findByUserId(String userId);

	/**
	 * userId로 해당되는 웹페이지의 html을 가져온다
	 * {'_id': ?0}: _id필드가 첫 번째 매개변수와 같은 문서를 찾아라
	 * {'html': 1}: 1->포함, 0->제외, html필드만 조회해라
	 * @param id webpage_id
	 * @return 웹 페이지의 html Optional
	 */
	@Query(value = "{'_id': ?0}", fields = "{'html': 1, '_id': 0}")
	Optional<HtmlDto> findHtmlById(ObjectId id);
}
