package com.kh.heallo.web.exception;

import com.kh.heallo.web.ResponseMsg;
import com.kh.heallo.web.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    //DataAccessException 처리(json)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseMsg> ExceptionHandler(DataAccessException e) {
        ResponseMsg responseMsg = new ResponseMsg()
                .setStatusCode(StatusCode.NOT_FOUND_ERROR)
                .setMessage("데이터를 찾을 수 없습니다.");

        return new ResponseEntity(responseMsg, HttpStatus.NOT_FOUND);
    }

}
