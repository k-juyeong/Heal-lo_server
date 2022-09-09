package com.kh.heallo.domain.review.svc;

import com.kh.heallo.domain.file.FileData;
import com.kh.heallo.domain.review.ReviewCriteria;
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
    public List<Review> findListByFcno(Long fcno, ReviewCriteria criteria) {
        List<Review> reviewList = reviewDAO.findListByFcno(fcno, criteria);

        //파일 요청

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
        Review review = reviewDAO.findByRvno(rvno);

        //파일 요청
//        review.setAttachedImage();
        return review;
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
        //리뷰저장
        Long rvno = reviewDAO.add(memno, fcno, review);

        //파일저장
        List<FileData> attachedImage = review.getAttachedImage();

        return rvno;
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
