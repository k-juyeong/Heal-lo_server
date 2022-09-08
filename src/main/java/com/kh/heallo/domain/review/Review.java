package com.kh.heallo.domain.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    private Long rvno;
    private String rvcontents;
    private double rvscore;
    private LocalDateTime rvcdate;
    private LocalDateTime rvudate;
    private double fcno;
    private double memno;
    private String memninkname;
    private List<Object> imageFiles;

}
