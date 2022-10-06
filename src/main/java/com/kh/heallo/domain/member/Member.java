package com.kh.heallo.domain.member;

import com.kh.heallo.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
  public static final String MEMCODE_NORMAL = "NORMAL";
  public static final String MEMCODE_SNS = "SNS";
  public static final String MEMCODE_ADMIN = "ADMIN";
  public static final String MEMSTATUS_JOIN = "JOIN";
  public static final String MEMSTATUS_WITHDRAW = "WITHDRAW";

  private Long memno;                   //number(8)
  private String memid;                 //varchar2(40)
  private String mempw;                 //varchar2(20)
  private String memtel;                //varchar2(13)
  private String memnickname;           //varchar2(30)
  private String mememail;              //varchar2(30)
  private String memname;               //varchar2(12)
  private String memcode;               //varchar2(15)
  private String memstatus;             //varchar2(15)

  @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
  private LocalDateTime memcdate;       //date

  @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
  private LocalDateTime memudate;       //date
  private Review review;
}
