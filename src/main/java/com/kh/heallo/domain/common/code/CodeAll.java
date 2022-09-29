package com.kh.heallo.domain.common.code;

import lombok.Data;

@Data
public class CodeAll {
  private  String pcode; //부모 코드 - BD000
  private  String pdecode; //부모 디코드 -게시판
  private  String ccode; //자식 코드 - BD001
  private  String cdecode; //부모 코드 - 자유게시판
}
