drop table uploadfile;
drop table bookmark;
drop table Board;
drop table Calendar;
drop table review;
drop table member;
drop table code;

drop sequence member_memno_seq;
drop sequence review_rvno_seq;
drop sequence uploadfile_ufno_seq;
drop sequence CALENDAR_CDNO_SEQ;
drop sequence board_BDNO_seq;
drop sequence bookmark_bmno_seq;

-- code 테이블
create table code (
      code              varchar2(10) primary key,
      code_name         varchar2(40),
      discript          varchar2(40),
      pcode             varchar2(10) references code(code),
      use_status        char(1) check(use_status in('Y','N')),
      cdate             timestamp,
      udate             timestamp
);

insert into code values('RV000','리뷰','',null,'Y',systimestamp,systimestamp);
insert into code values('BD000','게시판','',null,'Y',systimestamp,systimestamp);
insert into code values('CD000','캘린더','',null,'Y',systimestamp,systimestamp);

-- 회원
create table member (
        memno          number(8),
        memid          varchar2(40),
        mempw          varchar2(20),
        memtel         varchar2(13),
        memnickname    varchar2(30),
        mememail       varchar2(30),
        memname        varchar2(12),
        memcode        varchar2(15),
        memcdate       date,
        memudate       date
);

-- 제약조건
alter table member add constraint member_memno_pk primary key (memno);
alter table member modify memid constraint member_memid_nn NOT NULL;
alter table member modify mempw constraint member_mempw_nn NOT NULL;
alter table member modify memtel constraint member_memtel_nn NOT NULL;
alter table member modify memnickname constraint member_memninkname_nn NOT NULL;
alter table member modify mememail constraint member_mememail_nn NOT NULL;
alter table member modify memname constraint member_memname_nn NOT NULL;
alter table member modify memcdate constraint member_memcdate_nn NOT NULL;
alter table member modify memudate constraint member_memudate_nn NOT NULL;
alter table member add constraint memid_uk unique (memid);
alter table member add constraint memtel_uk unique (memtel);
alter table member add constraint memninkname_uk unique (memnickname);
alter table member add constraint mememail_uk unique (mememail);

-- 회원 시퀀스
create sequence member_memno_seq;

-- 즐겨찾기
CREATE TABLE Bookmark (
                          BMNO NUMBER(8),
                          MEMNO NUMBER(8),
                          FCNO NUMBER(8),
);
-- 제약조건
alter table bookmark add constraint bookmark_bmno_pk primary key (bmno);
alter table bookmark add constraint bookmark_fcno_fk foreign key (fcno)
    references facility(fcno);
alter table bookmark add constraint bookmark_memno_fk foreign key (memno)
    references member(memno);
alter table bookmark modify fcno constraint bookmark_fcno_nn not null;
alter table bookmark modify memno constraint bookmark_memno_nn not null;
alter table bookmark add constraint bookmark_fcno_memno_uk unique (fcno,memno);

-- 즐겨찾기 시퀀스
create sequence bookmark_bmno_seq;

-- 리뷰
create table review (
                        rvno         number(8),
                        rvcontents   clob,
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

-- 리뷰 시퀀스
create sequence review_rvno_seq;

-- 게시판
CREATE TABLE Board (
   BDNO NUMBER(8) PRIMARY KEY,
   BDCG NUMBER(10),
   BDTITLE VARCHAR2(150),
   MEMNO NUMBER(8) REFERENCES Member(memno),
   BDCONTENT CLOB,
   BDGROUP NUMBER(10) NOT NULL,
   BDDEPTH NUMBER(10) NOT NULL,
   BDVIEW NUMBER(10),
   BDHIT NUMBER(10),
   BDCDATE DATE,
   BDUDATE DATE
);

-- 게시판 시퀀스
create sequence board_BDNO_seq;

-- 캘린더
CREATE TABLE Calendar (
  CDNO NUMBER(8) PRIMARY KEY,
  MEMNO NUMBER(8)  REFERENCES Member(memno),
  CDCONTENT CLOB,
  CDRDATE DATE,
  CDCDATE DATE,
  CDUDATE DATE
);

-- 캘린더 시퀀스
CREATE SEQUENCE CALENDAR_CDNO_SEQ
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9999
    NOCYCLE
 NOCACHE;

-- 업로드 파일 테이블
create table uploadfile (
    ufno        number(8),
    code        varchar2(20),
    noid        number(8),
    ufsname     varchar2(200),
    uffname     varchar2(200),
    ufsize      number,
    uftype      varchar2(50),
    ufpath      varchar2(100),
    ufcdate     timestamp,
    ufudate     timestamp
);

-- 파일 시퀀스
create sequence uploadfile_ufno_seq;

-- 제약조건
alter table uploadfile add constraint uploadfile_ufno_pk primary key (ufno);
alter table uploadfile add constraint uploadfile_code_fk foreign key (code)
    references code(code);
alter table uploadfile modify code constraint uploadfile_code_nn not null;
alter table uploadfile modify noid constraint uploadfile_noid_nn not null;
alter table uploadfile modify ufsname constraint uploadfile_ufsname_nn not null;
alter table uploadfile modify uffname constraint uploadfile_uffname_nn not null;
alter table uploadfile modify ufsize constraint uploadfile_ufsize_nn not null;
alter table uploadfile modify uftype constraint uploadfile_uftype_nn not null;
alter table uploadfile modify ufpath constraint uploadfile_ufpath_nn not null;
alter table uploadfile add constraint uploadfile_ufsname_uk unique (ufsname);