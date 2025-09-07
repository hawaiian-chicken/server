package com.hachic.webi.chat.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hachic.webi.chat.domain.Message;

public interface MessageRepository extends MongoRepository<Message, String> {

	int countByConversationId(Long conversationId);
}
