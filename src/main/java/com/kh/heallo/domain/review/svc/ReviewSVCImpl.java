package com.kh.heallo.domain.review.svc;

import com.kh.heallo.domain.facility.dao.FacilityDAO;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.domain.uploadfile.AttachCode;
import com.kh.heallo.domain.uploadfile.FileData;
import com.kh.heallo.domain.review.ReviewCriteria;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.dao.ReviewDAO;
import com.kh.heallo.domain.uploadfile.svc.UploadFileSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ReviewSVCImpl implements ReviewSVC{

    private final ReviewDAO reviewDAO;
    private final FacilitySVC facilitySVC;
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

        //리뷰 조회
        List<Review> reviewList = reviewDAO.findListByFcno(fcno, criteria)
                .stream()
                .map(review -> {

                    //이미지 조회
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

        //리뷰 조회
        Review review = reviewDAO.findByRvno(rvno);

        //이미지파일 조회
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

        //리뷰 추가
        Long rvno = reviewDAO.add(review);

        //운동시설 평균평점 수정
        facilitySVC.updateToScore(review.getFcno());

        return rvno;
    }

    @Override
    public Long add(Review review, List<MultipartFile> files) {

        //리뷰 추가
        Long rvno = reviewDAO.add(review);

        //파일 업로드
        uploadFileSVC.fileUpload(AttachCode.RV_CODE, rvno, files);

        //운동시설 평균평점 수정
        facilitySVC.updateToScore(review.getFcno());

        return rvno;
    }

    /**
     * 리뷰수정
     * @param review
     * @return 결과 수
     */
    @Override
    public Integer update(Review review) {

        //리뷰 수정
        Integer resultCount = reviewDAO.update(review);

        //운동시설 평균평점 수정
        facilitySVC.updateToScore(review.getFcno());

        return resultCount;
    }

    @Override
    public Integer update(Review review, List<MultipartFile> files) {

        //리뷰 수정
        Integer resultCount = reviewDAO.update(review);

        //파일 업로드
        uploadFileSVC.fileUpload(AttachCode.RV_CODE, review.getRvno() ,files);

        //운동시설 평균평점 수정
        facilitySVC.updateToScore(review.getFcno());

        return resultCount;
    }

    @Override
    public Integer update(Review review, Long[] ufnoArr) {

        //리뷰 수정
        Integer resultCount = reviewDAO.update(review);

        //파일 삭제
        uploadFileSVC.delete(ufnoArr);

        //운동시설 평균평점 수정
        facilitySVC.updateToScore(review.getFcno());

        return resultCount;
    }

    @Override
    public Integer update(Review review, List<MultipartFile> files, Long[] ufnoArr) {

        //리뷰 수정
        Integer resultCount = reviewDAO.update(review);

        //파일 업로드
        uploadFileSVC.fileUpload(AttachCode.RV_CODE, review.getRvno() ,files);

        //파일 삭제
        uploadFileSVC.delete(ufnoArr);

        //운동시설 평균평점 수정
        facilitySVC.updateToScore(review.getFcno());

        return resultCount;
    }


    /**
     * 리뷰삭제
     * @param rvno
     * @return 결과 수
     */
    @Override
    public Integer delete(Long rvno, Long fcno) {

        //리뷰의 이미지파일 조회
        List<FileData> fileDataList = uploadFileSVC.findImages(AttachCode.RV_CODE, rvno);

        //리뷰 삭제
        Integer resultCount = reviewDAO.delete(rvno);

        //로컬 파일 삭제
        if (fileDataList != null) {
            Long[] ufArray = fileDataList.stream()
                    .map(fileData -> fileData.getUfno())
                    .toArray(Long[]::new);

            uploadFileSVC.delete(ufArray);
        }

        //운동시설 평균평점 수정
        facilitySVC.updateToScore(fcno);

        return resultCount;
    }

}
