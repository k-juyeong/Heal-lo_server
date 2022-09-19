package com.kh.heallo.web.member.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginForm {
  @NotBlank
  private String memid;       //아이디
  @NotBlank
  private String mempw;          //비밀번호
}

