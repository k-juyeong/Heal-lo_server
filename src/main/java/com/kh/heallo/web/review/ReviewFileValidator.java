package com.kh.heallo.web.review;

import com.kh.heallo.web.review.dto.ReviewFileData;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewFileValidator{
    public void validate(List<ReviewFileData> target, BindException bindException, int totalSize) throws BindException {

        if(totalSize > 5) {
            bindException.rejectValue("imageFiles","imageFiles.size",new Object[]{"5"},"파일 업로드 오류");
        }
        int NotSupportTypeLength = target
                .stream()
                .filter(multipartFile -> !multipartFile.getUftype().equals("image/jpeg") && !multipartFile.getUftype().equals("image/png"))
                .collect(Collectors.toList()).size();
        if (NotSupportTypeLength > 0) {
            bindException.rejectValue("imageFiles","imageFiles.type","파일 업로드 오류");
        }
        if (bindException.hasErrors()) throw bindException;
    }
}
