package com.hachic.webi.webpage.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record HtmlDto(
		@Schema(description = "몽고db에서 가져온 html") String html) {

	public HtmlDto(String html) {
		this.html = html;
	}
}
