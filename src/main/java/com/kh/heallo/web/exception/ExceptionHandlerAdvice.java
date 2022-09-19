package com.kh.heallo.web.exception;

import com.kh.heallo.web.response.FieldErrorDetail;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import com.kh.heallo.web.facility.controller.FacilityRestController;
import com.kh.heallo.web.review.controller.ReviewRestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = {ReviewRestController.class, FacilityRestController.class})
public class ExceptionHandlerAdvice {

    private final MessageSource messageSource;

    //JdbcTemplate query 결과값이 없으면 호출
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseMsg> ExceptionHandler(DataAccessException e) {
        e.printStackTrace();
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg
                .createHeader(StatusCode.DATA_NOT_FOUND_ERROR);

        return new ResponseEntity(responseMsg, HttpStatus.BAD_REQUEST);
    }

    //ModelAttribute 검증오류 발생 시 호출
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseMsg> ExceptionHandler(BindException e , Locale locale) {
    List<FieldErrorDetail> errorDetails =  e.getBindingResult().getFieldErrors().stream().map(fieldError -> {
                FieldErrorDetail fieldErrorDetail = FieldErrorDetail.create(fieldError, messageSource, locale);
                return fieldErrorDetail;
            }).collect(Collectors.toList());

        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg
                .createHeader(StatusCode.VALIDATION_ERROR)
                .setData("errors",errorDetails);

        return new ResponseEntity<>(responseMsg, HttpStatus.BAD_REQUEST);
    }

}
