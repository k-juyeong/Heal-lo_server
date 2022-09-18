package com.kh.heallo.web;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseMsg {
    private Header header;
    private Map<String, Object> data = new HashMap<>();

    @Data
    @AllArgsConstructor
    static class Header {
        private String code;
        private String message;
    }

    public ResponseMsg createHeader(StatusCode statusCode) {
        this.header = new Header(statusCode.getCode(), statusCode.getMessage());
        return this;
    }

    public ResponseMsg setData(String key, Object value) {
        data.put(key, value);
        return this;
    }
}



