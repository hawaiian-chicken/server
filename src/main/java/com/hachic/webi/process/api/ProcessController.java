package com.hachic.webi.process.api;

import com.hachic.webi.process.application.ProcessService;
import com.hachic.webi.process.dto.ProcessRequest;
import com.hachic.webi.process.dto.ProcessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/process-html")
@Tag(name = "Process API")
public class ProcessController {

    private final ProcessService filteringService;

    @Operation(summary = "요청받은 HTML을 LLM 서버에 전달하고 필터링 결과를 응답으로 반환하는 API")
    @PostMapping()
    public ResponseEntity<ProcessResponse> filterHtml(@RequestBody ProcessRequest request) throws IOException {
        return ResponseEntity.ok(filteringService.processHtml(request));
    }

}
