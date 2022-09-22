package com.kh.heallo.web.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditForm {
  private String memid;        //varchar2(40)
  private String  mempw;       //varchar2(20)
  private String memtel;       //varchar2(13)
  private String memnickname;  //varchar2(30)
  private String mememail;     //varchar2(30)
  private String memname;      //varchar2(12)
}
