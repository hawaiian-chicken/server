package com.hachic.webi.chat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document(collection = "auto_sequence")
public class AutoIncrementSequence {

	@Id
	private String id;
	private Long seq;
}
