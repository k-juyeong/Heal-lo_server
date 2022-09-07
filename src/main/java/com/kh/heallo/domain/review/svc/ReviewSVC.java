package com.kh.heallo.domain.review.svc;

import com.kh.heallo.domain.review.Review;

import java.util.List;

public interface ReviewSVC {

    /**
     * 리뷰 조회 결과 수
     * @param fcno
     * @return 결과 수
     */
    Integer getTotalCount(Long fcno);

    /**
     * 리뷰 조회(페이징)
     * @param Fcno
     * @param pageno
     * @return 리뷰 리스트
     */
    List<Review> findListByFcno(Long Fcno, Integer pageno);

    /**
     * 리뷰등록
     * @param fcno
     * @param memno
     * @return 결과 수
     */
    Integer add(Long memno,Long fcno);

    /**
     * 리뷰수정
     * @param fcno
     * @param memno
     * @return 결과 수
     */
    Integer update(Long memno,Long fcno);

    /**
     * 리뷰삭제
     * @param rvno
     * @return 결과 수
     */
    Integer delete(Long rvno);
}
