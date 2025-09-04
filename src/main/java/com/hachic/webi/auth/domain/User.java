package com.hachic.webi.auth.domain;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Getter
@Document(collection = "users")
public class User {

	@Id
	private ObjectId id;

	@Field("social_id")
	private final String socialId;

	private final String name;

	private final String provider;

	@Field("email")
	private final String email;

	@Field("created_at")
	@CreatedDate
	private LocalDateTime createdAt;

	@Field("updated_at")
	@LastModifiedDate
	private LocalDateTime updatedAt;

	public User(
			String socialId,
			String name,
			String provider,
			String email) {
		this.socialId = socialId;
		this.name = name;
		this.provider = provider;
		this.email = email;
	}
}
