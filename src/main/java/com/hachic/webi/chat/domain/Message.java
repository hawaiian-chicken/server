package com.hachic.webi.chat.domain;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Getter
@Document(collection = "messages")
@CompoundIndex(name = "conv_seq_unique", def = "{'conversation_id': 1, 'seq': 1}", unique = true)
public class Message {

	@Id
	private ObjectId id;

	@Field("conversation_id")
	private final Long conversationId;

	@Field("user_id")
	private final String userId;

	private final Role role;

	private final String content;

	@CreatedDate
	@Field("created_at")
	private Instant createdAt;

	public Message(Long conversationId,
					String userId,
					Role role,
					String content) {
		this.conversationId = conversationId;
		this.userId = userId;
		this.role = role;
		this.content = content;
	}

}
