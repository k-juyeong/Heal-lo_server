--회원 계정 조회(1회) - 닉네임, 아이디
select MEMNO, MEMID, MEMNICKNAME, MEMCDATE
  from MEMBER
 where MEMID like %아이디%
       or MEMNICKNAME like %닉네임%;

-- 회원 계정 목록조회
select MEMNO, MEMID, MEMNICKNAME, MEMCDATE
  from MEMBER
order by MEMNO asc;

-- 회원 계정 삭제
delete from MEMBER
       where MEMNO = '1';

-- 게시글 조회(1회) - 제목, 닉네임
select BDNO, BDTITLE, MEMNICKNAME, BDCDATE
  from BOARD t1, MEMBER t2
where t1.MEMNO = t2.MEMNO
   and BDTITLE like  %운동% ;

select BDNO, BDTITLE, MEMNICKNAME, BDCDATE
  from BOARD t1, MEMBER t2
where t1.MEMNO = t2.MEMNO
    and t2.MEMNICKNAME like %닉네임% ;

-- 댓글 조회(1회) - 댓글 내용, 닉네임
select RPNO, RPCONTENT, MEMNICKNAME, RPCDATE
  from REPLY t1, MEMBER t2
where t1.MEMNO = t2.MEMNO
   and RPCONTENT like %캘린더% ;

select RPNO, RPCONTENT, MEMNICKNAME, RPCDATE
  from REPLY t1, MEMBER t2
where t1.MEMNO = t2.MEMNO
   and t2.MEMNICKNAME like %닉네임%;

-- 댓글 목록 조회
select RPNO, RPCONTENT, MEMNICKNAME, RPCDATE
  from REPLY t1, MEMBER t2
where t1.MEMNO = t2.MEMNO
order by RPNO asc;

-- 문의글 조회(1회) - 제목, 닉네임
select BDNO, BDTITLE, MEMNICKNAME, BDCDATE
  from BOARD t1, MEMBER t2
where t1.MEMNO = t2.MEMNO
   and BDCG = 4
   and BDTITLE like %문의%;

select BDNO, BDTITLE, MEMNICKNAME, BDCDATE
  from BOARD t1, MEMBER t2
where t1.MEMNO = t2.MEMNO
   and BDCG = 4
   and t2.MEMNICKNAME like %닉네임% ;

-- 답글 등록
insert into REPLY
     values (REPLY_RPNO_SEQ.NEXTVAL, 4, REPLY_RPNO_SEQ.NEXTVAL, 0, '관리자 회원번호', '문의에 대한 답변', SYSDATE, SYSDATE);

-- 리뷰 조회(1회) - 리뷰내용, 닉네임, 운동시설
 select RVNO, RVCONTENT, MEMNICKNAME, FCNAME
   from REVIEW t1, MEMBER t2, FACILITY t3
 where t1.MEMNO = t2.MEMNO
    and t1.FCNO = t3.FCNO
    and RVCONTENT like %리뷰% ;

 select RVNO, RVCONTENT, MEMNICKNAME, FCNAME
   from REVIEW t1, MEMBER t2, FACILITY t3
 where t1.MEMNO = t2.MEMNO
    and t1.FCNO = t3.FCNO
    and t2.MEMNICKNAME like %닉네임%;

 select RVNO, RVCONTENT, MEMNICKNAME, FCNAME
   from REVIEW t1, MEMBER t2, FACILITY t3
 where t1.MEMNO = t2.MEMNO
    and t1.FCNO = t3.FCNO
    and t3.FCNAME like %요가%;

-- 리뷰 목록조회
select RVNO, RVCONTENT, MEMNICKNAME, FCNAME
  from REVIEW t1, MEMBER t2, FACILITY t3
where t1.MEMNO = t2.MEMNO
   and t1.FCNO = t3.FCNO
order by RVNO asc;

-- 운동시설 정보 수정
update FACILITY
     set FCTEL = '098-765-4321'
 where FCNO = '10';