package com.kh.heallo.web.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Clob;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddForm {
  private String cdContent;  // CDCONTENT	CLOB	Yes		3
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private String cdRDate;    // CDRDATE	DATE	Yes		4

  private List<MultipartFile> imageFiles;
}
