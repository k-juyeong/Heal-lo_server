-- -- 업로드 파일 테이블
-- create table uploadfile (
--     ufno        number(8),
--     bdno        number(8),
--     cdno        number(8),
--     rvno        number(8),
--     ufsname     varchar2(200),
--     uffname     varchar2(200),
--     ufsize      number,
--     uftype      varchar2(50),
--     ufpath      varchar(100),
--     ufcdate     timestamp,
--     ufudate     timestamp
-- );
-- -- 시퀀스
-- create sequence uploadfile_ufno_seq;
--
-- -- 제약조건
-- alter table uploadfile add constraint uploadfile_ufno_pk primary key (ufno);
-- alter table uploadfile add constraint uploadfile_bdno_fk foreign key (bdno)
--     references Board(bdno) ON DELETE CASCADE;
-- alter table uploadfile add constraint uploadfile_cdno_fk foreign key (cdno)
--     references Calendar(cdno) ON DELETE CASCADE;
-- alter table uploadfile add constraint uploadfile_rvno_fk foreign key (rvno)
--     references review(rvno) ON DELETE CASCADE;
-- alter table uploadfile add constraint uploadfile_ufsname_uk unique (ufsname);
-- alter table uploadfile modify ufsname constraint uploadfile_ufsname_nn not null;
-- alter table uploadfile modify ufudate constraint uploadfile_ufudate_nn not null;
-- alter table uploadfile modify ufpath constraint uploadfile_ufpath_nn not null;

-- new
-- 업로드 파일 테이블
create table uploadfile (
    ufno        number(8),
    code        varchar2(20),
    noid        number(8),
    ufsname     varchar2(200),
    uffname     varchar2(200),
    ufsize      number,
    uftype      varchar2(50),
    ufpath      varchar(100),
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
alter table uploadfile modify ufsname constraint uploadfile_ufsname_nn not null;
alter table uploadfile modify uffname constraint uploadfile_uffname_nn not null;
alter table uploadfile modify ufsize constraint uploadfile_ufsize_nn not null;
alter table uploadfile modify uftype constraint uploadfile_uftype_nn not null;
alter table uploadfile modify ufpath constraint uploadfile_ufpath_nn not null;
alter table uploadfile add constraint uploadfile_ufsname_uk unique (ufsname);

-- 파일 업로드 (리뷰)
insert into uploadfile
values(uploadfile_ufno_seq.nextval,'RV000',90,
       'd5qwd8-dqw85','테스트이미지',115123,'img/jpg','d:/test/tset',
       systimestamp,systimestamp);

-- 파일 조회 (리뷰)
select ufno,ufsname,uffname,uftype,ufpath,ufsize
from uploadfile
where code = 'RV000'
and noid = 123;