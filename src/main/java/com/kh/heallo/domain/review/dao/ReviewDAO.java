package com.kh.heallo.domain.review.dao;

import com.kh.heallo.domain.review.ReviewCriteria;
import com.kh.heallo.domain.review.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDAO {

    /**
     * 리뷰 조회 결과 수
     * @param fcno 운동시설 번호
     * @return 결과 수
     */
    Integer getTotalCount(Long fcno);

    /**
     * 리뷰 조회(페이징)
     * @param fcno 운동시설번호
     * @param criteria 검색조건
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
     * @param review 리뷰
     * @return 리뷰번호
     */
    Long add(Review review);

    /**
     * 리뷰수정
     * @param review 리뷰
     * @return 결과 수
     */
    Integer update(Review review);

    /**
     * 리뷰삭제
     * @param rvno 리뷰번호
     * @return 결과 수
     */
    Integer delete(Long rvno);

}
