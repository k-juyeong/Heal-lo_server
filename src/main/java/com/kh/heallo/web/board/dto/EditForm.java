package com.kh.heallo.web.board.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditForm {
  @NotNull
  private String bdcg;
  private Long no;
  private Long bdno;

  @NotBlank(message="제목을 입력해주세요.")
  private String bdtitle;

  @NotBlank(message="내용을 입력해주세요.")
  private String bdcontent;
  private Long memno;
  private String memnickname; //닉네임
}
