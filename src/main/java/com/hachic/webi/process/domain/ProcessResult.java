package com.hachic.webi.process.domain;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Getter
@Document(collection = "process_results")
public class ProcessResult {

	@Id
	private ObjectId id;

	@Field("user_id")
	private final String userId;

	@Field("user_request")
	private final String userRequest;

	@Field("webpage_id")
	private final ObjectId webpageId;

	@Field("request_type")
	private final String requestType;

	@Field("modified_html")
	private final String modifiedHtml;

	private final String message;

	@Field("created_at")
	@CreatedDate
	private LocalDateTime createdAt;

	public ProcessResult(
			String userId,
			String userRequest,
			ObjectId webpageId,
			String requestType,
			String modifiedHtml,
			String message) {
		this.userId = userId;
		this.userRequest = userRequest;
		this.webpageId = webpageId;
		this.requestType = requestType;
		this.modifiedHtml = modifiedHtml;
		this.message = message;
	}
}
