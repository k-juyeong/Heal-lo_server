-- 운동시설
create table facility (
    fcno         number(8),
    fcname       varchar2(100),
    fctype       varchar2(40),
    fchomepage   varchar2(400),
    fctel        varchar2(40),
    fclat        number,
    fclng        number,
    fcaddr       varchar2(200),
    fcpostcode   varchar2(10),
    fcstatus     VARCHAR2(20),
    fcimg        VARCHAR2(400),
    fcscore      NUMBER(2,1)
);

-- 제약조건
alter table facility add constraint facility_fcno_pk primary key (fcno);
alter table facility modify fcname constraint facility_fcname_nn not null;
alter table facility add constraint facility_fcscore_ck check (fcscore between 0 and 5);

-- 운동시설 시퀀스
create sequence facility_fcno_seq;

-- 운동시설 공공데이터 연동
MERGE INTO facility fc1
    USING (SELECT count(*) cnt from facility
           where fcname = '운동시설bb1'
             and fcaddr = '서울특별시 xxx') fc2
    ON (cnt > 0)
    WHEN MATCHED THEN update set
        fcname = '운동시설bb1',
        fctype = '당구장업',
        fchomepage = null,
        fctel = '010-1111-9999',
        fclat = 36.9626006263,
        fclng = 127.2392144698,
        fcaddr = '서울특별시 xxx',
        fcpostcode = 27472,
        fcstatus = '폐업'
    where fcname = '운동시설bb1'
                    or fcaddr = '서울특별시 xxx'
WHEN NOT MATCHED THEN insert values
                      (facility_fcno_seq.nextval, '운동시설bb1', '당구장업', null, '010-1111-9999',
                      36.9626006263, 127.2392144698, '서울특별시 xxx', 27472, '폐업',
                      'https://cdn.pixabay.com/photo/2020/04/03/20/49/gym-5000169_960_720.jpg',0);

-- 운동시설 조건검색
select fc.fcno,fc.fcname,fc.fctel,fc.fclat,fc.fclng,fc.fcaddr,fc.fcimg
from (select rownum rowno, facility.* from facility
      where fcname like '%%'
        and fcaddr like '울산광역시%'
        and fctype like '%당구장업%') fc
where fc.rowno > 0 and fc.rowno <= 10;

-- 운동시설 total count
select count(*) fctotal from facility
where fcname like '%%'
  and fcaddr like '%'
  and fctype like '%당구장업%';

-- 운동시설 평균평점 업데이트
update facility set
    fcscore = (select nvl(round(avg(rvscore),1),0) rvavg from review
               where review.fcno = 3)
where fcno = 1;

-- 운동시설 상세조회
select * from facility where fcno = 1;

-- 회원 즐겨찾기 정보조회
select fcno from bookmark
where memno = 1;

