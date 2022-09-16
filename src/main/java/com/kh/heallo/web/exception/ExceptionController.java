package com.kh.heallo.web.exception;

import com.kh.heallo.web.FieldErrorDetail;
import com.kh.heallo.web.ResponseMsg;
import com.kh.heallo.web.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class ExceptionController {

    private final MessageSource messageSource;

    //JdbcTemplate query 결과값이 없으면 호출
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseMsg> ExceptionHandler(DataAccessException e) {
        ResponseMsg responseMsg = new ResponseMsg()
                .setStatusCode(StatusCode.NOT_FOUND_ERROR)
                .setMessage("데이터를 찾을 수 없습니다.");

        return new ResponseEntity(responseMsg, HttpStatus.NOT_FOUND);
    }

    //ModelAttribute 검증오류 발생 시 호출
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseMsg> ExceptionHandler(BindException e , Locale locale) {
    List<FieldErrorDetail> errorDetails =  e.getBindingResult().getFieldErrors().stream().map(fieldError -> {
                FieldErrorDetail fieldErrorDetail = FieldErrorDetail.create(fieldError, messageSource, locale);
                return fieldErrorDetail;
            }).collect(Collectors.toList());

        ResponseMsg responseMsg = new ResponseMsg()
                    .setStatusCode(StatusCode.VALIDATION_ERROR)
                    .setMessage("입력값을 확인해주세요")
                    .setErrors(errorDetails);

        return new ResponseEntity<>(responseMsg, HttpStatus.BAD_REQUEST);
    }

}
