package com.hachic.webi.webpage.api;

import com.hachic.webi.webpage.application.WebpageService;
import com.hachic.webi.webpage.dto.WebpageRequest;
import com.hachic.webi.webpage.dto.WebpageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 웹 페이지 컨트롤러
 */
@RestController
@RequestMapping("/save-html")
@Tag(name = "webpage", description = "html 페이지 상호작용")
public class WebpageController {

    private final WebpageService webpageService;

    /**
     * 웹 페이지 컨트롤러 생성자
     *
     * @param webpageService 웹 페이지 서비스
     */
    public WebpageController(WebpageService webpageService) {
        this.webpageService = webpageService;
    }

    /**
     * 웹 페이지를 저장하고 UUID를 반환한다.
     *
     * @param webpageRequest 웹 페이지 요청
     * @return 웹 페이지 응답
     */
    @Operation(summary = "웹페이지 상호작용", description = "html 페이지 저장 후 uuid 반환")
    @PostMapping
    public ResponseEntity<WebpageResponse> saveWebpage(@Valid @RequestBody WebpageRequest webpageRequest) {
        String uuid = webpageService.saveHtml(webpageRequest.html());
        return ResponseEntity.ok(new WebpageResponse(uuid));
    }
}