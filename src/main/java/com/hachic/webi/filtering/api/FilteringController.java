package com.hachic.webi.filtering.api;

import com.hachic.webi.filtering.application.FilteringService;
import com.hachic.webi.filtering.dto.FilteringRequest;
import com.hachic.webi.filtering.dto.FilteringResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filtering")
@Tag(name = "Filtering API", description = "html을 llm  서버로 보내 필터링한 값을 가져오는 API")
public class FilteringController {

    private final FilteringService filteringService;

    @Operation(summary = "")
    @PostMapping
    public ResponseEntity<?> filterHtml(@RequestBody FilteringRequest request) {
        return null;
    }



}
