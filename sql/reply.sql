--  테이블삭제
  DROP TABLE REPLY;
  DROP SEQUENCE REPLY_RPNO_SEQ;


-- 댓글번호 시퀀스  
  CREATE SEQUENCE REPLY_RPNO_SEQ START WITH 1
  INCREMENT BY 1;
  
 
-- 댓글테이블
  CREATE TABLE REPLY (
	RPNO        NUMBER(8),
	BDNO 		    NUMBER(8),
	RPGROUP     NUMBER(10),
	RPDEPTH     NUMBER(10),
	MEMNO       NUMBER(8),
	RPCOMMENT 	CLOB,
	RPCDATE     timestamp default systimestamp,
	RPUDATE     timestamp default systimestamp
  );
  
 ALTER TABLE REPLY ADD CONSTRAINT REPLY_RPNO_PK PRIMARY KEY (RPNO);
 ALTER TABLE REPLY ADD CONSTRAINT REPLY_BDNO_FK FOREIGN KEY (BDNO)
      REFERENCES BOARD(BDNO);
 ALTER TABLE REPLY ADD CONSTRAINT REPLY_MEMNO_FK FOREIGN KEY (MEMNO)
      REFERENCES MEMBER(MEMNO);          
 ALTER TABLE REPLY MODIFY BDNO CONSTRAINT REPLY_BDNO_NN NOT NULL;      
 ALTER TABLE REPLY MODIFY MEMNO CONSTRAINT REPLY_MEMNO_NN NOT NULL;      
 ALTER TABLE REPLY MODIFY RPCOMMENT CONSTRAINT REPLY_RPCOMMENT_NN NOT NULL;      
 ALTER TABLE REPLY MODIFY RPCDATE CONSTRAINT BOARD_RPCDATE_NN NOT NULL;
 ALTER TABLE REPLY MODIFY RPUDATE CONSTRAINT BOARD_RPUDATE_NN NOT NULL;    
  
  
 
--게시판 추가 test
 INSERT INTO BOARD (BDNO, BDCG,BDTITLE,MEMNO,BDCONTENT)
 VALUES (BOARD_BDNO_SEQ.nextval, '자유게시판' ,'제목',1,'댓글내용입니다.');
 

--댓글 추가 test
 INSERT INTO REPLY(RPNO, BDNO,MEMNO,RPCOMMENT)
 VALUES (REPLY_RPNO_SEQ.nextval, BOARD_BDNO_SEQ.currval, 1,'댓글내용');
 
 SELECT * FROM REPLY;

-- 댓글 수
SELECT COUNT(RPNO)
  FROM REPLY
 WHERE BDNO=1;

-- 댓글 불러오기
SELECT *
  FROM REPLY
 WHERE BDNO=1;

-- 대댓글작성**
INSERT INTO REPLY
    VALUES (REPLY_RPNO_SEQ.NEXTVAL, 1,1(부모댓글),1(들여쓰기), 1, '대댓글', SYSDATE, SYSDATE);

-- 댓글 수정
UPDATE REPLY
   SET       RPCOMMENT = '수정댓글',
            RPUDATE = SYSDATE
 WHERE  RPNO = 1
   AND MEMNO = 1 ;

-- 댓글 삭제
DELETE FROM REPLY
WHERE RPNO = 1 AND MEMNO = 1;

UPDATE REPLY
   SET RPSTATUS = 'DELETED'
 WHERE RPNO = 1;

-- 대댓글의 경우 step을 한단계 더함
UPDATE REPLY
   SET RPSTEP = (RPSTEP) + 1 -- 이전 대댓글의 rpStep에 +1
 WHERE RPGROUP = 2  -- 같은 부모댓글
   AND RPNO = 1;   -- 해당 대댓글

-- 대댓글의 대댓글 경우 그 이후 rpStep 번호 수정 (+1)
UPDATE REPLY
   SET RPSTEP = RPSTEP + 1
 WHERE RPGROUP = 2  -- 같은 부모댓글
   AND RPNO >= 1;   -- 댓글

-- 'DELETED'의 상태의 댓글의 대댓글이 모두 삭제된 경우
-- 해당 댓글도 삭제
DELETE FROM REPLY
      WHERE RPSTATUS = 'DELETED'
        AND RPGROUP IN (SELECT RPGROUP
                          FROM REPLY
                        GROUP BY RPGROUP
                        HAVING COUNT(RPGROUP) = 1);
