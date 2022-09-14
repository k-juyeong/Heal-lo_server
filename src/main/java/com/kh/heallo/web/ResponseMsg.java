package com.kh.heallo.web;

import lombok.*;

import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMsg {

    public static final String STATUS_CODE_OK = "002";
    public static final String STATUS_CODE_VALIDATION_ERROR = "004";

    private String statusCode;
    private String message;
    private Object data;

    public static ResponseMsg create(String statusCode, String message, Object data) {

        return new ResponseMsg()
                .setStatusCode(statusCode)
                .setMessage(message)
                .setData(data);
    }

    public ResponseMsg setStatusCode(String statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public ResponseMsg setMessage(String message) {
        this.message = message;
        return this;
    }

    public ResponseMsg setData(Object data) {
        this.data = data;
        return this;
    }
}
