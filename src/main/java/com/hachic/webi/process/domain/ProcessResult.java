package com.hachic.webi.process.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.hachic.webi.process.dto.Actions;

import lombok.Getter;

@Getter
@Document(collection = "process_results")
public class ProcessResult {

	@Id
	private ObjectId id;

	@Field("user_id")
	private final String userId;

	@Field("user_message")
	private final String userMessage;

	@Field("webpage_id")
	private final ObjectId webpageId;

	@Field("request_type")
	private final String requestType;

/*	@Field("modified_html")
	private final String modifiedHtml;*/

	private final List<Actions> actions;

	@Field("ai_message")
	private final String aiMessage;

	@Field("created_at")
	@CreatedDate
	private LocalDateTime createdAt;

	public ProcessResult(
			String userId,
			String userMessage,
			ObjectId webpageId,
			String requestType,
			//String modifiedHtml,
			List<Actions> actions,
			String aiMessage) {
		this.userId = userId;
		this.userMessage = userMessage;
		this.webpageId = webpageId;
		this.requestType = requestType;
		//this.modifiedHtml = modifiedHtml;
		this.actions = actions;
		this.aiMessage = aiMessage;
	}
}
