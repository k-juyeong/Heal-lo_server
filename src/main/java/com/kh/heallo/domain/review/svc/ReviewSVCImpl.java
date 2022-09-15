package com.kh.heallo.domain.review.svc;

import com.kh.heallo.domain.uploadfile.FileData;
import com.kh.heallo.domain.review.ReviewCriteria;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.dao.ReviewDAO;
import com.kh.heallo.domain.uploadfile.dao.UploadFileDAO;
import com.kh.heallo.domain.uploadfile.svc.UploadFileSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewSVCImpl implements ReviewSVC{

    private final ReviewDAO reviewDAO;
    private final UploadFileSVC uploadFileSVC;

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
        List<Review> reviewList = reviewDAO.findListByFcno(fcno, criteria).stream().map(review -> {
            List<FileData> imageList = uploadFileSVC.findImagesByRvno(review.getRvno());
            review.setImageFiles(imageList);
            return review;
        }).collect(Collectors.toList());

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
        List<FileData> fileDataList = uploadFileSVC.findImagesByRvno(review.getRvno());
        review.setImageFiles(fileDataList);

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
        Integer[] resultCount = {0};
        Long rvno = reviewDAO.add(memno, fcno, review);

        List<FileData> fileDataList = review.getImageFiles();
        if(fileDataList != null) {
            fileDataList.stream().forEach(fileData -> {
                resultCount[0] += uploadFileSVC.ReviewFileUpload(rvno, fileData);
            });
            if (resultCount[0] == fileDataList.size()) {
                log.info("파일업로드 중 오류발생 {}",fileDataList);
            }
        }

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
    public Integer update(Long rvno, Review review, Long[] ufnoArr) {
        Integer[] resultCount = {0};
        Integer updateResultCount = reviewDAO.update(rvno, review);

        List<FileData> fileDataList = review.getImageFiles();
        if(fileDataList != null) {
            fileDataList.stream().forEach(fileData -> {
                resultCount[0] += uploadFileSVC.ReviewFileUpload(rvno, fileData);
            });
            if (resultCount[0] == fileDataList.size()) {
                log.info("파일업로드 중 오류발생 {}",fileDataList);
            }
        }
        if (ufnoArr.length > 0) {
            Arrays.stream(ufnoArr).forEach(ufno -> uploadFileSVC.delete(ufno));
        }

        return updateResultCount;
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
