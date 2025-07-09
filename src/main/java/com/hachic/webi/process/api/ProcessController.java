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

    @Operation(summary = "llm 서버에 html을 전달하고 수정된 html을 받아오는 api")
    @PostMapping()
    public ResponseEntity<ProcessResponse> filterHtml(@RequestBody ProcessRequest request) throws IOException {
        return ResponseEntity.ok(filteringService.filterHtml(request));
    }



}
