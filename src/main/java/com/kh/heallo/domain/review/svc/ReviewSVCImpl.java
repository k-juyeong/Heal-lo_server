package com.kh.heallo.domain.review.svc;

import com.kh.heallo.domain.uploadfile.AttachCode;
import com.kh.heallo.domain.uploadfile.FileData;
import com.kh.heallo.domain.review.ReviewCriteria;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.dao.ReviewDAO;
import com.kh.heallo.domain.uploadfile.svc.UploadFileSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewSVCImpl implements ReviewSVC{

    private final ReviewDAO reviewDAO;
    private final UploadFileSVC uploadFileSVC;


    /**
     * 리뷰 조회 결과 수
     * @param fcno
     * @return 결과 수
     */
    @Override
    public Integer getTotalCount(Long fcno) {
        return reviewDAO.getTotalCount(fcno);
    }

    /**
     * 리뷰 조회(페이징)
     * @param fcno
     * @param criteria
     * @return 리뷰 리스트
     */
    @Override
    public List<Review> findListByFcno(Long fcno, ReviewCriteria criteria) {
        List<Review> reviewList = reviewDAO.findListByFcno(fcno, criteria)
                .stream()
                .map(review -> {
                    List<FileData> imageList = uploadFileSVC.findImages(AttachCode.RV_CODE,review.getRvno());
                    review.setImageFiles(imageList);
                    return review;
                })
                .collect(Collectors.toList());

        return reviewList;
    }

    /**
     * 리뷰 단일조회
     * @param rvno 리뷰번호
     * @return 리뷰
     */
    @Override
    public Review findByRvno(Long rvno) {
        Review review = reviewDAO.findByRvno(rvno);
        List<FileData> fileDataList = uploadFileSVC.findImages(AttachCode.RV_CODE, review.getRvno());
        review.setImageFiles(fileDataList);

        return review;
    }

    /**
     * 리뷰등록
     * @return 결과 수
     */
    @Override
    public Long add(Review review) {
        Long rvno = reviewDAO.add(review);

        return rvno;
    }

    @Override
    public Long add(Review review, List<MultipartFile> files) {
        Long rvno = reviewDAO.add(review);
        uploadFileSVC.fileUpload(AttachCode.RV_CODE, rvno, files);

        return rvno;
    }

    /**
     * 리뷰수정
     * @param review
     * @return 결과 수
     */
    @Override
    public Integer update(Review review) {
        Integer resultCount = reviewDAO.update(review);

        return resultCount;
    }

    @Override
    public Integer update(Review review, List<MultipartFile> files) {
        Integer resultCount = reviewDAO.update(review);
        uploadFileSVC.fileUpload(AttachCode.RV_CODE, review.getRvno() ,files);

        return resultCount;
    }

    @Override
    public Integer update(Review review, Long[] ufnoArr) {
        Integer resultCount = reviewDAO.update(review);
        uploadFileSVC.delete(ufnoArr);

        return resultCount;
    }

    @Override
    public Integer update(Review review, List<MultipartFile> files, Long[] ufnoArr) {
        Integer resultCount = reviewDAO.update(review);
        uploadFileSVC.fileUpload(AttachCode.RV_CODE, review.getRvno() ,files);
        uploadFileSVC.delete(ufnoArr);

        return resultCount;
    }

    /**
     * 리뷰삭제
     * @param rvno
     * @return 결과 수
     */
    @Override
    public Integer delete(Long rvno) {
        List<FileData> fileDataList = uploadFileSVC.findImages(AttachCode.RV_CODE, rvno);
        Integer resultCount = reviewDAO.delete(rvno);
        if (fileDataList != null) {
            Long[] ufArray = fileDataList.stream()
                    .map(fileData -> fileData.getUfno())
                    .toArray(Long[]::new);
            uploadFileSVC.delete(ufArray);
        }

        return resultCount;
    }
}
