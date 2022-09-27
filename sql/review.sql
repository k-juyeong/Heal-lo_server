-- 리뷰
create table review (
    rvno         number(8),
    rvcontents   clob,
    rvline       number(2),
    rvscore      number(2,1),
    rvcdate      date,
    rvudate      date,
    memno        number(8),
    fcno         number(8)
);

-- 제약조건
alter table review add constraint review_rvno_pk primary key (rvno);
alter table review add constraint review_fcno_fk foreign key (fcno)
    references facility(fcno);
alter table review add constraint review_memno_fk foreign key (memno)
    references member(memno);
alter table review modify rvcontents constraint review_rvcontents_nn not null;
alter table review modify rvscore constraint review_rvscore_nn not null;
alter table review modify rvcdate constraint review_rvcdate_nn not null;
alter table review modify rvudate constraint review_rvudate_nn not null;
alter table review modify fcno constraint review_fcno_nn not null;
alter table review modify memno constraint review_memno_nn not null;
alter table review add constraint review_rvscore_ck check (rvscore between 0 and 5);
alter table review add constraint review_rvline_ck check (rvline <= 50);

-- 시퀀스
create sequence review_rvno_seq;

-- 리뷰 등록
insert into review
values(review_rvno_seq.nextval, '시설이 좋아요1', 4.5, sysdate, sysdate, 1, 1);

-- 리뷰 수정
update review set
                  rvcontents = '시설이 별로에요',
                  rvscore = 1.5,
                  rvudate = sysdate
where review.rvno = 1;

--  리뷰 조회(페이징)
select *
from (select rownum rvnum, review.* ,member.memnickname
      from (select * from review
            order by rvcdate desc) review, member
      where review.memno = member.memno
        and review.fcno = 4535)
where rvnum > 0 and rownum <= 10;

-- 리뷰 단일조회
select * from review where rvno = 1;

-- 리뷰 total count 조회
select count(fcno) rvtotal from review
where fcno = 1;

-- 리뷰 삭제
delete review
  where rvno = 2;
