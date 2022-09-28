package com.kh.heallo.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ResponseMsg {
    private Header header;
    private Map<String, Object> data = new HashMap<>();
    private List<Object> list = new ArrayList<>();

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

    public ResponseMsg setData(Object value) {
        list = (List<Object>) value;
        return this;
    }

    public ResponseMsg setData(List<Object> value) {
        list = value;
        return this;
    }

}



