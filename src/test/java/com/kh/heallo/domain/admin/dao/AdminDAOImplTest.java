package com.kh.heallo.domain.admin.dao;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.member.dao.MemberDAO;
import com.kh.heallo.domain.reply.Reply;
import com.kh.heallo.domain.review.Review;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class AdminDAOImplTest {

  @Autowired
  private AdminDAO  adminDAO;
  private MemberDAO memberDAO;

  @Test
  @DisplayName("회원 계정 목록 전체")
  void memberList() {
    List<Member> memberAll = adminDAO.memberList();

    log.info("memberAll={}", memberAll.size());
  }

  @Test
  @DisplayName("회원 계정 검색")
  void memberListByIdOrNickname() {
    Member member = new Member();
    member.setMemid("member1");
    member.setMempw("12345678");
    member.setMemtel("010-1234-5678");
    member.setMemnickname("테스터1");
    member.setMememail("test1@test.com");
    member.setMemname("테스터");
    member.setMemcode("normal");
    member.setMemcode("JOIN");

    memberDAO.join(member);

    List<Member> members = adminDAO.memberListByIdOrNickname("테");
    log.info("members={}", members.size());

//    members.stream().map(member1 -> {
//      log.info("member={}", member1);
//      return 0;
//    });
  }

  @Test
  @DisplayName("전체 게시글 목록")
  void boardList() {
    List<Board> boardAll = adminDAO.boardList();

    log.info("boardAll={}", boardAll.size());

//    boardAll.stream().map(board -> {
//      log.info("board={}", board);
//      return 0;
//    });
  }

  @Test
  @DisplayName("전체 게시글 제목 조회")
  void boardListByTitle() {
    String title = "정보";
    List<Board> boardList = adminDAO.boardListByTitle(title);

    log.info("boardList={}", boardList.size());

//    boardList.stream().forEach(board -> {
//      log.info("board={}", board);
//    });
  }

  @Test
  @DisplayName("전체 게시글 작성자 조회")
  void boardListByIdOrNickname() {
    String user = "테스터";

    List<Board> boardList = adminDAO.boardListByIdOrNickname(user);

    log.info("boardList={}", boardList.size());

//    boardList.stream().forEach(board -> {
//      log.info("board={}", board);
//    });
  }

  @Test
  @DisplayName("댓글 목록")
  void replyList() {
    List<Reply> replyAll = adminDAO.replyList();

    log.info("replyAll={}", replyAll.size());

//    replyAll.stream().forEach(reply -> {
//      log.info("reply={}", reply);
//    });
  }

  @Test
  @DisplayName("댓글 내용 조회")
  void replyListByContent() {
    String content = "시설";

    List<Reply> replyList = adminDAO.replyListByContent(content);

    log.info("reply={}", replyList.size());

//    replyList.stream().forEach(reply -> {
//      log.info("reply={}", reply);
//    });
  }

  @Test
  @DisplayName("댓글 작성자 조회")
  void replyListByIdOrNickname() {
    String user = "테스터";

    List<Reply> replyList = adminDAO.replyListByIdOrNickname(user);

    log.info("replyList={}", replyList.size());

//    replyList.stream().forEach(reply -> {
//      log.info("reply={}", reply);
//    });
  }

  @Test
  @DisplayName("리뷰 목록")
  void reviewList() {
    List<Review> reviewAll = adminDAO.reviewList();

    log.info("reviewAll={}", reviewAll.size());

//    reviewAll.stream().forEach(review -> {
//      log.info("review={}", review);
//    });
  }

  @Test
  @DisplayName("리뷰 내용 조회")
  void reviewListByContent() {
    String content = "시설";

    List<Review> reviewList = adminDAO.reviewListByContent(content);
    log.info("reviewList={}", reviewList.size());

//    reviewList.stream().forEach(review -> {
//      log.info("review={}", review);
//    });
  }

  @Test
  @DisplayName("리뷰 운동시설 조회")
  void reviewListByFacility() {
    String fcName = "헬스";

    List<Review> reviewList = adminDAO.reviewListByFacility(fcName);
    log.info("reviewList={}", reviewList.size());

//    reviewList.stream().forEach(review -> {
//      log.info("review={}", review);
//    });
  }

  @Test
  @DisplayName("리뷰 작성자 조회")
  void reviewListByIdOrNickname() {
    String user = "테스터";

    List<Review> reviewList = adminDAO.reviewListByIdOrNickname(user);
    log.info("reviewList={}", reviewList.size());

//    reviewList.stream().forEach(review -> {
//      log.info("review={}", review);
//    });
  }

  @Test
  @DisplayName("문의글 목록")
  void noticeList() {
    List<Board> noticeAll = adminDAO.noticeList();

    log.info("noticeAll={}", noticeAll.size());

//    noticeAll.stream().forEach(notice -> {
//      log.info("notice={}", notice);
//    });
  }

  @Test
  @DisplayName("목록 제목 조회")
  void noticeListByTitle() {
    String title = "문의글";

    List<Board> noticeList = adminDAO.noticeListByTitle(title);
    log.info("noticeList={}", noticeList.size());

//    noticeList.stream().forEach(notice -> {
//      log.info("notice={}", notice);
//    });
  }

  @Test
  @DisplayName("목록 작성자 조회")
  void noticeListByIdOrNickname() {
    String user = "테스터";

    List<Board> noticeList = adminDAO.noticeListByIdOrNickname(user);
    log.info("noticeList={}", noticeList.size());

//    noticeList.stream().forEach(notice -> {
//      log.info("notice={}", notice);
//    });
  }

}