package com.kh.heallo.domain.bookmark.svc;

import com.kh.heallo.domain.bookmark.Bookmark;
import com.kh.heallo.domain.facility.Facility;

import java.util.List;

public interface BookmarkSVC {

    /**
     * 로그인 계정 즐겨찾기 목록 조회
     * @param memno 회원번호
     * @return 즐겨찾기 리스트
     */
    List<Bookmark> findBookmarkListByMemno(Long memno);

    /**
     *
     * @param fcno 운동시설번호
     * @param memno 회원번호
     * @return 상태값
     */
    Boolean replace(Long memno, Long fcno);

    /**
     * 즐겨찾기 페이지 리스트
     * @return
     */
    List<Facility> bookmarkPageList();

}
