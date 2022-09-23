package com.kh.heallo.domain.member.dao;

import com.kh.heallo.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    Long joinedMember = memberDAO.join(member);
    Member findedMember = memberDAO.findById(joinedMember);
    log.info("findedMember={}",findedMember);
  }

  @Test
  @DisplayName("회원조회")
  @Order(2)
  void findById(){
    Long memno = 41L;
    Member findedMember = memberDAO.findById(memno);

    Assertions.assertThat(findedMember.getMemno()).isEqualTo(41);
    Assertions.assertThat(findedMember.getMemid()).isEqualTo("naim");
    Assertions.assertThat(findedMember.getMempw()).isEqualTo("1234");
    Assertions.assertThat(findedMember.getMemtel()).isEqualTo("010-1111-1111");
    Assertions.assertThat(findedMember.getMemnickname()).isEqualTo("테스터");
    Assertions.assertThat(findedMember.getMememail()).isEqualTo("test@test.com");
    Assertions.assertThat(findedMember.getMemname()).isEqualTo("테스터1");
    Assertions.assertThat(findedMember.getMemcode()).isEqualTo("normal");

  }

  @Test
  @DisplayName("수정")
  @Order(3)
  void update() {
    Member member = new Member();
    String memid = "naim123";
    member.setMemname("테스트1");
    member.setMemnickname("테슷트1");
    member.setMememail("test1@test.com");
    member.setMempw("12341");
    member.setMemtel("010-1111-2221");
    member.setMemid(memid);

<<<<<<< HEAD
=======
    memberDAO.update(Long.valueOf(memid), member);
>>>>>>> 8cc8b01b4f3ca1031db36546771003f8c8534f10
    Long memno = 2L;
    memberDAO.update(memno, member);
    Member findedMember = memberDAO.findById(memno);
    Assertions.assertThat(findedMember.getMemname()).isEqualTo("테스트1");
    Assertions.assertThat(findedMember.getMemnickname()).isEqualTo("테슷트1");
    Assertions.assertThat(findedMember.getMememail()).isEqualTo("test1@test.com");
    Assertions.assertThat(findedMember.getMempw()).isEqualTo("12341");
    Assertions.assertThat(findedMember.getMemtel()).isEqualTo("010-1111-2221");
  }

  @Test
  @DisplayName("삭제")
  @Order(4)
  void del(){
    String memId = "proteen1";

    memberDAO.del(memId);
    Long memno = 24L;
    Member findedMember = memberDAO.findById(memno);
    Assertions.assertThat(findedMember).isNull();
  }
}
