package com.hachic.webi.process.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Getter
@Document(collection = "process_results")
public class ProcessResult {

	@Id
	private String id;

	@Field("user_id")
	private final String userId;

	@Field("user_request")
	private final String userRequest;

	@Field("webpage_uuid")
	private final String webpageUuid;

	@Field("request_type")
	private final String requestType;

	@Field("modified_html")
	private final String modifiedHtml;

	private final String message;

	public ProcessResult(
			String userId,
			String userRequest,
			String webpageUuid,
			String requestType,
			String modifiedHtml,
			String message) {
		this.userId = userId;
		this.userRequest = userRequest;
		this.webpageUuid = webpageUuid;
		this.requestType = requestType;
		this.modifiedHtml = modifiedHtml;
		this.message = message;
	}
}
