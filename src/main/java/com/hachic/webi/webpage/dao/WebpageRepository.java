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
	 * userId로 해당되는 웹페이지의 html을 가져옵니다
	 * {'_id': ?0}: _id 필드가 첫 번째 매개변수와 일치하는 문서를 찾습니다
	 * {'html': 1}: html 필드만 포함하여 조회합니다 (1: 포함, 0: 제외)
	 * @param id 웹페이지의 ID (webpage_id)
	 * @return 웹페이지의 HTML을 담은 Optional 객체
	 */
	@Query(value = "{'_id': ?0}", fields = "{'html': 1, '_id': 0}")
	Optional<HtmlDto> findHtmlById(ObjectId id);
}
