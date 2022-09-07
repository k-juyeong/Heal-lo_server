package com.kh.heallo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMsg {
    private String StatusCode;
    private String message;
    private Object data;
}
