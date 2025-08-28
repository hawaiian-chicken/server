package com.hachic.webi.webpage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hachic.webi.webpage.domain.Webpage;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 웹 페이지 응답 DTO
 */
public record WebpageResponse(

		@Schema(description = "요청 고유 ID")
		@JsonProperty("user_id")
		String userId,

		@Schema(description = "웹 페이지 ID")
		@JsonProperty("webpage_id")
		String webpageId

) {

	/**
	 * Webpage 엔티티를 WebpageResponse DTO로 변환한다.
	 *
	 * @param webpage Webpage 엔티티
	 * @return 변환된 WebpageResponse DTO
	 */
	public static WebpageResponse from(Webpage webpage) {
		return new WebpageResponse(
				webpage.getUserId(),
				webpage.getId().toHexString()
		);
	}
}
