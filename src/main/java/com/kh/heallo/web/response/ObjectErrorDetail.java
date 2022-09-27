package com.kh.heallo.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Locale;

@Data
@AllArgsConstructor
public class ObjectErrorDetail {

    private String field;
    private String code;
    private String message;

    public static ObjectErrorDetail create(ObjectError objectError, MessageSource messageSource, Locale locale) {
        return new ObjectErrorDetail(
                objectError.getObjectName(),
                objectError.getCode(),
                messageSource.getMessage(objectError, locale));
    }
}
