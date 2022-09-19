package com.kh.heallo.web.member.session;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginMember {
  private String memid;
  private String memnickname;
}
