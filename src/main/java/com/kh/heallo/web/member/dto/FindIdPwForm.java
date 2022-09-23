package com.kh.heallo.web.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindIdPwForm {
  private String memid;
  private String memname;      //varchar2(12)
  private String mememail;     //varchar2(30)
}
