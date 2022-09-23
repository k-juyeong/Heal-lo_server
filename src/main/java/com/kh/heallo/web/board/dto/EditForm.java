package com.kh.heallo.web.board.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditForm {

  @NotNull
  private String bdcg;

  private Long bdno;

  @NotBlank
  private String bdtitle;

  @NotBlank
  private String bdcontent;

  private Long memno;

  private String memnickname; //닉네임

}
