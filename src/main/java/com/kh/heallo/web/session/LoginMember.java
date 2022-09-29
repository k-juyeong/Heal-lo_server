package com.kh.heallo.web.session;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginMember {
  private Long memno;
  private String memnickname;
}
