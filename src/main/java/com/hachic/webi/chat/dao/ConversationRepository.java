package com.hachic.webi.chat.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hachic.webi.chat.domain.Conversation;

public interface ConversationRepository extends MongoRepository<Conversation, Long> {
}
