package com.kh.heallo.web.board.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class SaveForm {
  @NotNull
  private String bdcg;

//  @NotBlank(message="제목을 입력해주세요.")
  private String bdtitle;
//  @NotBlank(message="내용을 입력해주세요.")
  private String bdcontent;
  private Long memno;
  private String memnickname; //닉네임
}
