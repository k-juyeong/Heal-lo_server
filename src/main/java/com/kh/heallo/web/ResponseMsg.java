package com.kh.heallo.web;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMsg {

    private String statusCode;
    private String statusMessage;
    private String message;
    private Map<String,Object> data = new HashMap<>();

    public ResponseMsg setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode.getStatusCode();
        this.statusMessage = statusCode.getMessage();
        return this;
    }

    public ResponseMsg setMessage(String message) {
        this.message = message;
        return this;
    }

    public ResponseMsg setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
