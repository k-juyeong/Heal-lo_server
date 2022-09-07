package com.kh.heallo.domain.facility.svc;

import com.kh.heallo.domain.facility.Bookmark;
import com.kh.heallo.domain.facility.Criteria;
import com.kh.heallo.domain.facility.Facility;

import java.util.List;

public interface FacilitySVC {

    /**
     * 공공데이터 연동
     */
    int connect();

    /**
     * 운동시설 조건검색
     * @param criteria 검색조건
     * @return 운동시설리스트
     */
    List<Facility> search(Criteria criteria);

    /**
     * 운동시설 조건검색 결과 수
     * @param criteria 검색조건
     * @return 결과 수
     */
    int getTotalCount(Criteria criteria);

    /**
     * 운동시설 평균평점 수정
     * @param fcno 운동시설번호
     * @return  결과 수
     */
    int updateToScore(Long fcno);

    /**
     * 운동시설 상세검색
     * @param fcno 운동시설번호
     * @return  운동시설
     */
    Facility findByFcno(Long fcno);

    /**
     * 로그인 계정 즐겨찾기 목록 조회
     * @param memno 회원번호
     * @return 즐겨찾기 리스트
     */
    List<Bookmark> findBookmarkListByMemno(Long memno);

    /**
     * 즐겨찾기추가
     * @param fcno 운동시설번호
     * @param memno 회원번호
     * @return 즐겨찾기 번호
     */
    Long addBookmark(Long memno, Long fcno);

    /**
     * 즐겨찾기 삭제
     * @param bmno 즐겨찾기번호
     * @return 결과 수
     */
    int deleteBookmark(Long bmno);

}
