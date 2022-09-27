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