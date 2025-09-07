package com.hachic.webi.process.api;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hachic.webi.process.application.ProcessService;
import com.hachic.webi.process.dto.ProcessRequest;
import com.hachic.webi.process.dto.ProcessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/process-html")
@Tag(name = "Process API")
public class ProcessController {

	private final ProcessService processService;

	@Operation(summary = "요청받은 HTML을 LLM 서버에 전달하고 필터링 결과를 응답으로 반환하는 API")
	@PostMapping()
	public ResponseEntity<ProcessResponse> processHtml(
			// @AuthenticationPrincipal OAuth2ResourceServerProperties.Jwt jwt,
			@Valid @RequestBody ProcessRequest request) throws IOException {
		// TODO: 로그인 후 인증된 유저로 변경
		String userId = "hachic";
		return ResponseEntity.ok(processService.processHtml(request, userId));
	}

}
