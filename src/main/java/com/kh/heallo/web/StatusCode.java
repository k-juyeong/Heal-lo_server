package com.kh.heallo.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum StatusCode {
    SUCCESS("001","성공"),
    VALIDATION_ERROR("002","검증오류"),
    NOT_FOUND_ERROR("003","데이터베이스오류");

    private String statusCode;
    private String message;
}
