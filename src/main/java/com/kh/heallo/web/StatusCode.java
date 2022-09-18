package com.kh.heallo.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum StatusCode {
    SUCCESS("00","응답이 정상적으로 이루어졌습니다."),
    VALIDATION_ERROR("01","검증에 문제가 생겼습니다."),
    DATA_NOT_FOUND_ERROR("02","값을 찾을수 없습니다."),
    SERVER_ERROR("99","서버에 문제가 생겼습니다.");

    private String code;
    private String message;
}
