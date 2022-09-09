package com.kh.heallo.web.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long rvno;
    private double memno;
    private String rvcontents;
    private double rvscore;
    private String rvcdate;
    private String memninkname;
    private List<ReviewFileData> imageFiles;

    public void setRvcdate(LocalDateTime rvcdate) {
        String format = rvcdate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.rvcdate = format;
    }
}
