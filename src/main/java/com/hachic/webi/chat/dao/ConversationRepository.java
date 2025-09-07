package com.hachic.webi.chat.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hachic.webi.chat.domain.Conversation;

public interface ConversationRepository extends MongoRepository<Conversation, Long> {
	/**
	 * 특정 유저가 생성한 채팅방을 리스트로 모두 반환하는 메서드
	 * @param userId 유저 ID
	 * @return userId가 일치하는 모든 conversation List
	 */
	List<Conversation> findAllByUserId(String userId);
}
