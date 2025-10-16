package com.hachic.webi.chat.domain;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Document(collection = "conversations")
public class Conversation {

	@Transient // 영속성 필드에서 제외
	public static final String SEQUENCE_NAME = "conversation_sequence";

	@Id @Setter
	private Long id;

	private String title;

	@Field("user_id")
	private final String userId;

	private List<String> tags;

	@Field("msg_count")
	private int msgCount;

	@LastModifiedDate
	@Field("last_message_at")
	private Instant lastMessageAt;

	public Conversation(String userId) {
		this.userId = userId;
		this.msgCount = 0;
	}

	/**
	 * conversation에 첫 message를 바탕으로 title과 tag를 등록함
	 * @param title AI가 정한 conversation의 제목
	 * @param tags AI가 추출한 conversation의 tag
	 */
	public void setTitleAndTags(String title, List<String> tags) {
		this.title = title;
		this.tags = tags;
	}

	/**
	 * Message가 생성되면 conversation 안의 메시지 개수를 업데이트하고 lastMessageAt을 갱신함
	 * @param msgCount conversations 안의 메시지 개수
	 */
	public void addMessage(int msgCount) {
		this.msgCount = msgCount;
	}
}
