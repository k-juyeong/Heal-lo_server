package com.kh.heallo.domain.review.svc;

import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.ReviewCriteria;
import org.springframework.web.multipart.MultipartFile;

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
     * @param review 리뷰내용
     * @return 결과 수
     */
    Long add(Review review);
    Long add(Review review, List<MultipartFile> files);

    /**
     * 리뷰수정
     * @param review
     * @return 결과 수
     */
    Integer update(Review review);
    Integer update(Review review, List<MultipartFile> files);
    Integer update(Review review, Long[] ufnoArr);

    Integer update(Review review, List<MultipartFile> files, Long[] ufnoArr);

    /**
     * 리뷰삭제
     * @param rvno, fcno
     * @return 결과 수
     */
    Integer delete(Long rvno, Long fcno);
}
