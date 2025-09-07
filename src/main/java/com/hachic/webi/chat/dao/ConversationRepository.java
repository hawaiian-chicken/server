package com.hachic.webi.chat.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hachic.webi.chat.domain.Conversation;

public interface ConversationRepository extends MongoRepository<Conversation, Long> {
	List<Conversation> findAllByUserId(String userId);
}
