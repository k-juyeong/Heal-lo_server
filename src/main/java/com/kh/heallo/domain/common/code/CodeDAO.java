package com.kh.heallo.domain.common.code;

import com.kh.heallo.web.Code;

import java.util.List;

public interface CodeDAO {
  /**
   * 상위코드 입력시 하위코드 반환
   *
   * @param pcode 상위코드
   * @return 하위코드, 디코드
   */
  List<Code> code(String pcode);

  /**
   * 모든코드 반환
   * @return
   */
  List<CodeAll> codeAll();
}
