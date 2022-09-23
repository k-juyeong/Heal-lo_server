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

-- 운동시설 리뷰 최근업로드 이미지 조회
select * from (select uf.* from review rv, uploadfile uf
               where fcno = 863
                 and  rv.rvno = uf.noid
               order by ufcdate desc)
where rownum <= 5;