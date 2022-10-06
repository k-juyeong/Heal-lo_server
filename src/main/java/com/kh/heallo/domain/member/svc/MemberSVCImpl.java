package com.kh.heallo.domain.member.svc;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.member.Member;
import com.kh.heallo.domain.member.dao.MemberDAO;
import com.kh.heallo.domain.reply.Reply;
import com.kh.heallo.domain.review.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberSVCImpl implements MemberSVC{

  private final MemberDAO memberDAO;

  /**
   * 가입
   *
   * @param member 가입정보
   * @return 가입건수
   */
  @Override
  public Long join(Member member) {
    return memberDAO.join(member);
  }

  /**
   * 조회 BY 회원번호
   *
   * @param memno 회원번호
   * @return 회원정보
   */
  @Override
  public Member findBymemno(Long memno) {
    return memberDAO.findBymemno(memno);
  }

  /**
   * 조회 BY 회원번호
   *
   * @param id 회원아이디
   * @return 회원정보
   */
  @Override
  public Member findById(String id) {
    return memberDAO.findById(id);
  }

  /**
   * 조회 BY 회원번호
   *
   * @param email 회원이메일
   * @return 회원정보
   */
  @Override
  public Member findByEmail(String email) {
    return memberDAO.findByEmail(email);
  }


  /**
   * 수정
   *
   * @param memno  아이디
   * @param member 수정할 정보
   * @return 수정건수
   */
  @Override
  public void update(Long memno, Member member) {
    memberDAO.update(memno,member);
  }

  @Override
  public void updateStatus(Long memno, Member member) {
    memberDAO.updateStatus(memno,member);
  }

  /**
   * 탈퇴
   *
   * @param memid 비밀번호
   * @return
   */
  @Override
  public void del(String memid) {
    memberDAO.del(memid);
  }

  /**
   * 로그인
   * @param memid 아이디
   * @param mempw 비밀번호
   * @return  회원
   */
  @Override
  public Optional<Member> login(String memid, String mempw) {

    return memberDAO.login(memid,mempw);
  }

  /**
   * 아이디 찾기
   *
   * @param memname  이름
   * @param mememail 이메일
   * @return 아이디
   */
  @Override
  public Member findId(String memname, String mememail) {
    return memberDAO.findId(memname,mememail);
  }

  /**
   * 비밀번호 찾기
   *
   * @param memid    아이디
   * @param memname  이름
   * @param mememail 이메일
   * @return
   */
  @Override
  public Member findPw(String memid, String memname, String mememail) {
    return memberDAO.findPw(memid,memname,mememail);
  }

  /**
   * 로그인 계정 작성 게시글 조회
   *
   * @param memno 회원번호
   * @return
   */
  @Override
  public List<Board> findBoardByMemno(Long memno) {
    return memberDAO.findBoardByMemno(memno);
  }

  /**
   * 로그인 계정 작성 댓글 조회
   *
   * @param memno 회원번호
   * @return
   */
  @Override
  public List<Reply> findReplyByMemno(Long memno) {
    return memberDAO.findReplyByMemno(memno);
  }

  /**
   * 로그인 계정 작성 리뷰 조회
   *
   * @param memno 회원번호
   * @param rvno  리뷰번호
   * @return 리뷰내용
   */
  @Override
  public List<Review> findReviewByMemno(Long memno, Long rvno) {
    return memberDAO.findReviewByMemno(memno, rvno);
  }

  /**
   * 아이디 중복체크
   *
   * @param memid 아이디
   * @return 존재하면 true
   */
  @Override
  public Boolean dupChkOfMemid(String memid) {
    return memberDAO.dupChkOfMemid(memid);
  }

  /**
   * 전화번호 중복체크
   *
   * @param memtel 아이디
   * @return 존재하면 true
   */
  @Override
  public Boolean dupChkOfMemtel(String memtel) {
    return memberDAO.dupChkOfMemtel(memtel);
  }

  /**
   * 이메일 중복체크
   *
   * @param mememail 아이디
   * @return 존재하면 true
   */
  @Override
  public Boolean dupChkOfMememail(String mememail) {
    return memberDAO.dupChkOfMememail(mememail);
  }

  /**
   * 닉네임 중복체크
   *
   * @param memnickname 닉네임
   * @return 존재하면 true
   */
  @Override
  public Boolean dupChkOfMemnickname(String memnickname) {
    return memberDAO.dupChkOfMemnickname(memnickname);
  }
}
