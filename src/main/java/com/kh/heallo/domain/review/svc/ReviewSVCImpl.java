package com.kh.heallo.domain.review.svc;

import com.kh.heallo.domain.review.Criteria;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.dao.ReviewDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewSVCImpl implements ReviewSVC{

    private final ReviewDAO reviewDAO;

    /**
     * 리뷰 조회 결과 수
     *
     * @param fcno
     * @return 결과 수
     */
    @Override
    public Integer getTotalCount(Long fcno) {
        return reviewDAO.getTotalCount(fcno);
    }

    /**
     * 리뷰 조회(페이징)
     *
     * @param fcno
     * @param criteria
     * @return 리뷰 리스트
     */
    @Override
    public List<Review> findListByFcno(Long fcno, Criteria criteria) {
        List<Review> reviewList = reviewDAO.findListByFcno(fcno, criteria);

        //반목문으로 리뷰에 해당하는 이미지 파일 요청
        //
        //

        return reviewList;
    }

    /**
     * 리뷰 단일조회
     *
     * @param rvno 리뷰번호
     * @return 리뷰
     */
    @Override
    public Review findByRvno(Long rvno) {
        return reviewDAO.findByRvno(rvno);
    }

    /**
     * 리뷰등록
     *
     * @param memno
     * @param fcno
     * @return 결과 수
     */
    @Override
    public Long add(Long memno, Long fcno, Review review) {
        return reviewDAO.add(memno, fcno, review);
    }

    /**
     * 리뷰수정
     *
     * @param rvno
     * @param review
     * @return 결과 수
     */
    @Override
    public Integer update(Long rvno, Review review) {
        return reviewDAO.update(rvno, review);
    }

    /**
     * 리뷰삭제
     *
     * @param rvno
     * @return 결과 수
     */
    @Override
    public Integer delete(Long rvno) {
        return reviewDAO.delete(rvno);
    }
}
