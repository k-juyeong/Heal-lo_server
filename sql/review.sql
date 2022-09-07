-- 리뷰
create table review (
    rvno         number(8),
    rvcontents   clob,
    rvscore      number(2,1),
    rvcdate      date,
    rvudate      date,
    fcno         number(8),
    memno        number(8)
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

-- 시퀀스
create sequence review_rvno_seq;