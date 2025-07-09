package com.hachic.webi.dto;

import lombok.Getter;

@Getter
public class WebpageResponse {

    private final String uuid;

    public WebpageResponse(String uuid) {

        this.uuid = uuid;
    }
}
