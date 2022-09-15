package com.kh.heallo.domain.review.svc;

import com.kh.heallo.domain.review.ReviewCriteria;
import com.kh.heallo.domain.review.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewSVC {

    /**
     * 리뷰 조회 결과 수
     * @param fcno
     * @return 결과 수
     */
    Integer getTotalCount(Long fcno);

    /**
     * 리뷰 조회(페이징)
     * @param fcno
     * @param criteria
     * @return 리뷰 리스트
     */
    List<Review> findListByFcno(Long fcno, ReviewCriteria criteria);

    /**
     * 리뷰 단일조회
     * @param rvno 리뷰번호
     * @return 리뷰
     */
    Review findByRvno(Long rvno);

    /**
     * 리뷰등록
     * @param fcno
     * @param memno
     * @param review 리뷰내용
     * @return 결과 수
     */
    Long add(Long memno,Long fcno, Review review);

    /**
     * 리뷰수정
     * @param rvno
     * @param review
     * @return 결과 수
     */
    Integer update(Long rvno,Review review,Long[] ufnoArr);

    /**
     * 리뷰삭제
     * @param rvno
     * @return 결과 수
     */
    Integer delete(Long rvno);
}
