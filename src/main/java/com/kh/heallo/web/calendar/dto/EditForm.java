package com.kh.heallo.web.calendar.dto;

import com.kh.heallo.domain.uploadfile.FileData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditForm {
  private String cdContent;  // CDCONTENT	CLOB	Yes		3
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private String cdRDate;    // CDRDATE	DATE	Yes		4

  // 파일 첨부
  private List<MultipartFile> imageFiles;

  // 파일 참조
  private List<FileData> foundImageFiles;
}
