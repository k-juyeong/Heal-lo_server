package com.kh.heallo.web.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditForm {
  private String cdContent;  // CDCONTENT	CLOB	Yes		3
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private String cdRDate;    // CDRDATE	DATE	Yes		4

}
