package com.kh.heallo.web.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindIdForm {
  @NotEmpty(message = "이름과 이메일을 입력해주세요")
  private String memid;
  @NotEmpty(message = "이름을 입력해주세요")
  private String memname;      //varchar2(12)
  @NotEmpty(message = "이메일을 입력해주세요")
  private String mememail;     //varchar2(30)
}
