package com.kh.heallo.web.calendar.dto;

import com.kh.heallo.domain.uploadfile.FileData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Clob;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayForm {
  private String cdContent;  // CDCONTENT	CLOB	Yes		3
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private String cdRDate;    // CDRDATE	DATE	Yes		4
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime cdCDate; // CDCDATE	DATE	Yes		5
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime cdUDate; // CDUDATE	DATE	Yes		5

  // 파일 참조
  private List<FileData> foundImageFiles;
}
