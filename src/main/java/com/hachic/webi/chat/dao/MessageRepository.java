package com.hachic.webi.chat.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hachic.webi.chat.domain.Message;

public interface MessageRepository extends MongoRepository<Message, String> {

	/**
	 * 특정 채팅방 내의 메시지 개수를 세는 메서드
	 * @param conversationId 채팅방 ID
	 * @return 메시지 개수
	 */
	int countByConversationId(Long conversationId);

	/**
	 * 특정 채팅방 내의 메시지를 리스트로 모두 반환하는 메서드
	 * @param conversationId 채팅방 ID
	 * @return 메시지 List
	 */
	List<Message> findAllByConversationId(Long conversationId);
}
