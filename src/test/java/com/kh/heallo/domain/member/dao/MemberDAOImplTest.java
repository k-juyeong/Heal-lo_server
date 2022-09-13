package com.kh.heallo.domain.member.dao;

import com.kh.heallo.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberDAOImplTest {

  @Autowired
  private MemberDAO memberDAO;

  @Test
  @DisplayName("회원등록")
  @Order(1)
  void join(){
    Member member = new Member();
    member.setMemid("proteen1");
    member.setMempw("naim1111");
    member.setMemtel("010-1234-5618");
    member.setMemnickname("테스터1");
    member.setMememail("test1@test.com");
    member.setMemname("박지훈");
    member.setMemcode("normal");
    member.setMemcdate(new Date());
    member.setMemudate(new Date());

    int joinedMember = memberDAO.join(member);
    log.info("joinedMember={}",joinedMember);
  }

  @Test
  @DisplayName("회원조회")
  @Order(2)
  void findByPw(){
    Member member = new Member();
    Member findedMember = memberDAO.findByPw(member.getMempw());

    Assertions.assertThat(findedMember.getMemno()).isEqualTo(member.getMemno());
    Assertions.assertThat(findedMember.getMemid()).isEqualTo(member.getMemid());
    Assertions.assertThat(findedMember.getMempw()).isEqualTo(member.getMempw());
    Assertions.assertThat(findedMember.getMemtel()).isEqualTo(member.getMemtel());
    Assertions.assertThat(findedMember.getMemninkname()).isEqualTo(member.getMemninkname());
    Assertions.assertThat(findedMember.getMememail()).isEqualTo(member.getMememail());
    Assertions.assertThat(findedMember.getMemname()).isEqualTo(member.getMemname());
    Assertions.assertThat(findedMember.getMemcode()).isEqualTo(member.getMemcode());

  }

  @Test
  @DisplayName("수정")
  @Order(3)
  void update(){
    Member member = new Member();
    String memmpw = member.getMempw();
    member.setMempw(memmpw);
    member.setMemninkname("로니콜먼");

    memberDAO.update(memmpw,member);

    Member findedMember = memberDAO.findByPw(memmpw);
    Assertions.assertThat(findedMember.getMemninkname()).isEqualTo(member.getMemninkname());
  }

  @Test
  @DisplayName("삭제")
  @Order(4)
  void del(){
    Member member = new Member();
    String memmpw = member.getMempw();

    memberDAO.del(memmpw);

    Member findedMember = memberDAO.findByPw(memmpw);
    Assertions.assertThat(findedMember).isNull();
  }
}
