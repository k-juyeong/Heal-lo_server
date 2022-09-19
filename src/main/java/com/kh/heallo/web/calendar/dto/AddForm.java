package com.kh.heallo.web.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Clob;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddForm {
  private Clob cdContent;  // CDCONTENT	CLOB	Yes		3
  private String cdRDate;    // CDRDATE	DATE	Yes		4
}
