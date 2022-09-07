-- 즐겨찾기
CREATE TABLE Bookmark (
    BMNO NUMBER(8),
    MEMNO NUMBER(8),
    FCNO NUMBER(8)
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

-- 시퀀스
create sequence bookmark_bmno_seq;

-- 회원의 즐겨찾기 리스트 조회
select * from bookmark
where memno = 1;

-- 즐겨찾기 추가
insert into bookmark
values(bookmark_bmno_seq.nextval, 1, 1);

-- 즐겨찾기 삭제
delete bookmark
 where  bmno = 1