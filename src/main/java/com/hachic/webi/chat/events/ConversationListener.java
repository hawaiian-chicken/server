package com.hachic.webi.chat.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.hachic.webi.chat.application.SequenceGeneratorService;
import com.hachic.webi.chat.domain.Conversation;

import lombok.RequiredArgsConstructor;

/**
 * 새로운 Conversation이 추가될 때마다 자동으로 id값이 올라가도록 하는 Listener 클래스
 */
@RequiredArgsConstructor
@Component
public class ConversationListener extends AbstractMongoEventListener<Conversation> {

	private final SequenceGeneratorService sequenceGenerator;

	@Override
	public void onBeforeConvert(BeforeConvertEvent<Conversation> event) {
		event.getSource().setId(sequenceGenerator.generateSequence(Conversation.SEQUENCE_NAME));
	}
}
