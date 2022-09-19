package com.kh.heallo.web.response;

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
    NOT_LOGIN_ERROR("03","로그인이 필요한 서비스입니다."),
    NOT_MATCHING_MEMBER_ERROR("04","해당 서비스는 본인만 이용 가능합니다."),
    SERVER_ERROR("99","서버에 문제가 생겼습니다.");

    private String code;
    private String message;
}
