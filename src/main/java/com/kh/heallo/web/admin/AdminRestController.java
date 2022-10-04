package com.kh.heallo.web.admin;


import com.kh.heallo.domain.admin.svc.AdminSVC;
import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.board.svc.BoardSVC;
import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.member.svc.MemberSVC;
import com.kh.heallo.domain.reply.Reply;
import com.kh.heallo.domain.reply.svc.ReplySVC;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.svc.ReviewSVC;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestController {

  private final AdminSVC adminSVC;
  private final MemberSVC memberSVC;
  private final BoardSVC boardSVC;
  private final ReplySVC replySVC;
  private final ReviewSVC reviewSVC;

  // 회원 계정 전체 목록
  @GetMapping("/memberAll")
  public ResponseEntity<ResponseMsg> members() {
    List<Member> memberAll = adminSVC.memberList();

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS)
        .setData("memberAll", memberAll);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 회원 닉네임, 아이디 검색
  @GetMapping("/member/{memInfo}")
  public ResponseEntity<ResponseMsg> memberByMemInfo(@PathVariable String memInfo) {
    List<Member> memberList = adminSVC.memberListByIdOrNickname(memInfo);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("memberList", memberList);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 회원 계정 삭제
  @DeleteMapping("/member/{memid}")
  public ResponseEntity<ResponseMsg> memberDel(@PathVariable String memid){
    memberSVC.del(memid);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }


  // 게시물 - 게시글 목록
  @GetMapping("/boardAll")
  public ResponseEntity<ResponseMsg> boardAll() {
    List<Board> boardAll = adminSVC.boardList();

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("boardAll", boardAll);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 게시글 제목 검색
  @GetMapping("/board/list/{title}")
  public ResponseEntity<ResponseMsg> boardByTitle(@PathVariable String title) {
    List<Board> boardList = adminSVC.boardListByTitle(title);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("boardList", boardList);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 게시글 작성자 검색
  @GetMapping("/board/{memInfo}")
  public ResponseEntity<ResponseMsg> boardByMemInfo(@PathVariable String memInfo) {
    List<Board> boardList = adminSVC.boardListByIdOrNickname(memInfo);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("boardList", boardList);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 게시글 삭제
  @DeleteMapping("board/{bdno}")
  public ResponseEntity<ResponseMsg> boardDel(@PathVariable Long bdno) {
    boardSVC.deleteByBoardId(bdno);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 댓글 목록
  @GetMapping("/replyAll")
  public ResponseEntity<ResponseMsg> replyAll() {
    List<Reply> replyAll = adminSVC.replyList();

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("replyAll", replyAll);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 댓글 내용 검색
  @GetMapping("reply/list/{content}")
  public ResponseEntity<ResponseMsg> replyByContent(@PathVariable String content) {
    List<Reply> replyList = adminSVC.replyListByContent(content);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("replyList", replyList);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 댓글 작성자 검색
  @GetMapping("reply/{memInfo}")
  public ResponseEntity<ResponseMsg> replyByMemInfo(@PathVariable String memInfo) {
    List<Reply> replyList = adminSVC.replyListByIdOrNickname(memInfo);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("replyList", replyList);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 댓글 삭제
  @DeleteMapping("/reply/{rpno}")
  public ResponseEntity<ResponseMsg> replyDel(@PathVariable Long rpno) {
    replySVC.delete(rpno);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 리뷰 목록
  @GetMapping("/reviewAll")
  public ResponseEntity<ResponseMsg> reviewAll() {
    List<Review> reviewAll = adminSVC.reviewList();

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("reviewAll", reviewAll);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 리뷰 내용 검색
  @GetMapping("review/list/{content}")
  public ResponseEntity<ResponseMsg> reviewByContent(@PathVariable String content) {
    List<Review> reviewList = adminSVC.reviewListByContent(content);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("reviewList", reviewList);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 리뷰 운동시설 검색
  @GetMapping("review/search/{fcName}")
  public ResponseEntity<ResponseMsg> reviewByFcName(@PathVariable String fcName) {
    List<Review> reviewList = adminSVC.reviewListByFacility(fcName);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("reviewList", reviewList) ;
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 리뷰 작성자 검색
  @GetMapping("review/{memInfo}")
  public ResponseEntity<ResponseMsg> reviewByMemInfo(@PathVariable String memInfo) {
    List<Review> reviewList = adminSVC.reviewListByIdOrNickname(memInfo);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS)
            .setData("reviewList", reviewList);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 리뷰 삭제
  @DeleteMapping("review/{rvno}")
  public ResponseEntity<ResponseMsg> reviewDel(@PathVariable Long rvno) {
    reviewSVC.delete(rvno);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
            .createHeader(StatusCode.SUCCESS);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 문의글 목록
  @GetMapping("/noticeAll")
  public ResponseEntity<ResponseMsg> noticeAll() {
    List<Board> noticeAll = adminSVC.noticeList();

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS)
        .setData("noticeAll", noticeAll);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 문의글 제목 검색
  @GetMapping("/notice/list/{title}")
  public ResponseEntity<ResponseMsg> noticeByTitle(@PathVariable String title) {
    List<Board> noticeList = adminSVC.noticeListByTitle(title);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS)
        .setData("noticeList", noticeList);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }

  // 게시물 - 문의글 작성자 검색
  @GetMapping("/notice/{memInfo}")
  public ResponseEntity<ResponseMsg> noticeByMemInfo(@PathVariable String memInfo) {
    List<Board> noticeList = adminSVC.noticeListByIdOrNickname(memInfo);

    // Create ResponseEntity
    ResponseMsg responseMsg = new ResponseMsg()
        .createHeader(StatusCode.SUCCESS)
        .setData("noticeList", noticeList);
    return new ResponseEntity<>(responseMsg, HttpStatus.OK);
  }


  // 운동시설 정보 수정
}

