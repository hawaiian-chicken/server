package com.hachic.webi.webpage.api;

import com.hachic.webi.application.WebpageService;
import com.hachic.webi.dto.WebpageRequest;
import com.hachic.webi.dto.WebpageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/save-html")
public class WebpageController {

    // uuid 반환
    private final WebpageService webpageService;

    public WebpageController(WebpageService webpageService) {
        this.webpageService = webpageService;
    }

    @PostMapping
    public ResponseEntity<WebpageResponse> saveWebpage(@RequestBody WebpageRequest webpageRequest) {

        String uuid = webpageService.saveHtml(webpageRequest.getHtml());
        return ResponseEntity.ok(new WebpageResponse(uuid));
    }
}
