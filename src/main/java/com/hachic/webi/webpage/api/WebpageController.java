package com.hachic.webi.webpage.api;

import com.hachic.webi.application.WebpageService;
import com.hachic.webi.dto.WebpageRequest;
import com.hachic.webi.dto.WebpageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/save-html")
@Tag(name = "webpage", description = "html 페이지 상호작용")
public class WebpageController {

    // uuid 반환
    private final WebpageService webpageService;

    public WebpageController(WebpageService webpageService) {
        this.webpageService = webpageService;
    }

    @Operation(summary = "웹페이지 상호작용", description = "html 페이지 저장 후 uuid 반환")
    @PostMapping
    public ResponseEntity<WebpageResponse> saveWebpage(@RequestBody WebpageRequest webpageRequest) {

        String uuid = webpageService.saveHtml(webpageRequest.html());
        return ResponseEntity.ok(new WebpageResponse(uuid));
    }
}
