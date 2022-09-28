-- 시퀀스 생성
CREATE SEQUENCE CALENDAR_CDNO_SEQ
 START WITH 1
 INCREMENT BY 1
 MINVALUE 1
 MAXVALUE 9999
 NOCYCLE
 NOCACHE;

--테이블 생성
CREATE TABLE Calendar (
CDNO NUMBER(8) PRIMARY KEY,
MEMNO NUMBER(8)  REFERENCES Member(memno),
CDCONTENT CLOB,
CDRDATE DATE,
CDCDATE DATE,
CDUDATE DATE
);

--운동기록 등록
insert into calendar (cdno, memno, cdContent, cdrdate, cdcdate, cdudate)
  values (calendar_cdno_seq.nextval, 2, '오늘 운동함', '2022/09/18', sysdate, sysdate);


insert into calendar
  values (calendar_cdno_seq.nextval, 2, '오늘 운동함', '2022/09/18', sysdate, sysdate);
insert into calendar
  values (calendar_cdno_seq.nextval, 1, '오늘 운동함22', '2022/09/20', sysdate, sysdate);

--운동기록 조회(1회)
select t1.*
  from calendar t1, member t2
 where t1.memno = t2.memno
   and to_char(cdrdate, 'YYYY-MM-DD') = '2022-09-18';

select *
  from calendar
where to_char(cdrdate, 'YYYY-MM-DD') = '2022-09-18'
  and memno = 1;

--운동기록 조회(1달)
select cdcontent, cdrdate, cdcdate
  from calendar
 where to_char(cdrdate, 'YYYY-MM-DD') between '2022-09-01' and '2022-09-30'
   and memno = 2;

--운동기록 수정
update CALENDAR
   set CDCONTENT = ''
 where to_char(cdrdate, 'YYYY-MM-DD') = '2022/09/20';

--운동기록 삭제
delete from CALENDAR
      where to_char(cdrdate, 'YYYY-MM-DD') = '20220831'
        and memno = 2;


select * from calendar;
