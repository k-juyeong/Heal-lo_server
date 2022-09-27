package com.kh.heallo.web.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayForm {
  private String cdContent;  // CDCONTENT	CLOB	Yes		3
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private String cdRDate;    // CDRDATE	DATE	Yes		4
  private LocalDateTime cdCDate; // CDCDATE	DATE	Yes		5
  private LocalDateTime cdUDate; // CDUDATE	DATE	Yes		5

}
