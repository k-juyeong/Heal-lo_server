package com.kh.heallo.domain.calendar;

import lombok.Data;

import java.sql.Clob;
import java.time.LocalDateTime;

@Data
public class Calendar {
  private Integer cdno;    // CDNO	NUMBER(8,0)	No		1
  private Integer memno;   // MEMNO	NUMBER(8,0)	Yes		2
  private Clob cdContent;  // CDCONTENT	CLOB	Yes		3
  private String cdRDate;    // CDRDATE	DATE	Yes		4
  private LocalDateTime cdCDate; // CDCDATE	DATE	Yes		5
  private LocalDateTime cdUDate; // CDUDATE	DATE	Yes		6
}
