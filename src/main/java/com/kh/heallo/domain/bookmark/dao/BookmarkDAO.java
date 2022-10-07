package com.kh.heallo.domain.bookmark.dao;

import com.kh.heallo.domain.bookmark.Bookmark;
import com.kh.heallo.domain.facility.Facility;

import java.util.List;
import java.util.Optional;

public interface BookmarkDAO {

    /**
     * 로그인 계정 즐겨찾기 목록 조회
     * @param memno 회원번호
     * @return  즐겨찾기 리스트
     */
    List<Bookmark> findBookmarkListByMemno(Long memno);

    /**
     * 즐겨찾기 레코드 유무 확인
     * @param memno
     * @param fcno
     * @return
     */
    Optional<Bookmark> CheckingBookmark(Long memno, Long fcno);

    /**
     * 즐겨찾기추가
     * @param fcno 운동시설번호
     * @param memno 회원번호
     * @return 결과 수
     */
    Long addBookmark(Long memno, Long fcno);


    /**
     * 즐겨찾기 삭제
     * @param bmno 즐겨찾기번호
     * @return 결과 수
     */
    Integer deleteBookmark(Long bmno);

    /**
     * 즐겨찾기 페이지 리스트
     * @return
     */
    List<Facility> bookmarkPageList(String order, Long memno);
}
