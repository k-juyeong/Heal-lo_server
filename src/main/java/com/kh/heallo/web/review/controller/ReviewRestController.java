package com.kh.heallo.web.review.controller;

import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.domain.uploadfile.FileData;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.svc.ReviewSVC;
import com.kh.heallo.web.ResponseMsg;
import com.kh.heallo.web.StatusCode;
import com.kh.heallo.web.review.ReviewFileValidator;
import com.kh.heallo.web.utility.FileSetting;
import com.kh.heallo.web.utility.DtoModifier;
import com.kh.heallo.web.review.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewRestController {

    private final ReviewSVC reviewSVC;
    private final FacilitySVC facilitySVC;
    private final FileSetting fileSetting;
    private final DtoModifier dtoModifier;
    private final ReviewFileValidator fileValidator;

    //리뷰 total 조회
    @GetMapping("/{fcno}/total")
    public ResponseEntity<ResponseMsg> getTotalCount(@PathVariable("fcno") Long fcno) {
        Integer totalCount = reviewSVC.getTotalCount(fcno);

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("totalCount", totalCount);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    //해당 운동시설 리뷰 리스트 조회(페이징)
    @GetMapping("/{fcno}/list")
    public ResponseEntity<ResponseMsg> findListByFcno(
            @PathVariable("fcno") Long fcno,
            @ModelAttribute ReviewCriteriaDto searchCriteria
    ) {
        //Test 회원
        long memno = 1L;

        switch(searchCriteria.getOrderBy()) {
            case "dateAsc": searchCriteria.setOrderBy("rvcdate asc");
            break;
            case "dateDesc": searchCriteria.setOrderBy("rvcdate desc");
            break;
            case "scoreDsc": searchCriteria.setOrderBy("rvscore desc");
            break;
            default: searchCriteria.setOrderBy("rvcdate asc");
        }

        List<ReviewDto> reviewDtos = reviewSVC
                .findListByFcno(fcno, dtoModifier.getReviewCriteria(searchCriteria))
                .stream()
                .map(review -> {
                    ReviewDto reviewDto = dtoModifier.getReviewDto(review, memno);
                    return reviewDto;
                })
                .collect(Collectors.toList());

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("reviews", reviewDtos);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    //리뷰 등록 처리
    @PostMapping("/{fcno}")
    public ResponseEntity add(
            @PathVariable("fcno") Long fcno,
            @Valid @ModelAttribute AddReviewForm addReviewForm,
            @RequestParam(required = false) List<MultipartFile> multipartFiles
    ) throws BindException {

        //Test 회원
        long memno = 1L;

        List<FileData> fileDataList = null;
        if (multipartFiles != null) {
            //파일 검증
            List<ReviewFileData> reviewFileDataList = dtoModifier.getReviewFileData(multipartFiles);
            addReviewForm.setImageFiles(reviewFileDataList);
            fileValidator.validate(
                    reviewFileDataList,
                    new BindException(addReviewForm, "addReviewForm"),
                    reviewFileDataList.size()
            );

            //파일저장
            fileDataList = multipartFiles
                    .stream()
                    .map(multipartFile -> {
                        FileData fileData = null;
                        try {
                            String localFileName = fileSetting.transForTo(multipartFile);
                            fileData = fileSetting.getFileData(multipartFile, localFileName);
                        } catch (IOException e) {
                            log.info("파일 저장중 exception 발생 {}", e.getMessage());
                        }
                        return fileData;
                    })
                    .collect(Collectors.toList());
        }

        Review review = dtoModifier.getReviewByAddReviewForm(addReviewForm);
        review.setFcno(fcno);
        review.setMemno(memno);
        review.setImageFiles(fileDataList);
        Long rvno = reviewSVC.add(memno, fcno, review);
        facilitySVC.updateToScore(fcno);

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("rvno", rvno)
                .setData("redirect", "/facilities/" + fcno);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    //리뷰 수정 처리
    @PatchMapping("/{rvno}")
    public ResponseEntity<ResponseMsg> update(
            @PathVariable("rvno") Long rvno,
            @Valid @ModelAttribute EditReviewForm editReviewForm,
            @RequestParam(required = false) List<MultipartFile> multipartFiles
    ) throws BindException {

        //Test 회원
        long memno = 1L;

        List<FileData> fileDataList = null;
        if (multipartFiles != null) {
            //파일검증
            List<ReviewFileData> reviewFileDataList = dtoModifier.getReviewFileData(multipartFiles);
            editReviewForm.setImageFiles(reviewFileDataList);
            Review review = reviewSVC.findByRvno(rvno);
            int totalSize = review.getImageFiles().size()
                    - editReviewForm.getDeleteImages().length
                    + reviewFileDataList.size();
            fileValidator.validate(
                    reviewFileDataList,
                    new BindException(editReviewForm, "addReviewForm"),
                    totalSize);

            //파일저장
            fileDataList = multipartFiles
                    .stream()
                    .map(multipartFile -> {
                    FileData fileData = null;
                        try {
                            String localFileName = fileSetting.transForTo(multipartFile);
                            fileData = fileSetting.getFileData(multipartFile, localFileName);
                        } catch (IOException e) {
                            log.info("파일 저장중 exception 발생 {}", e.getMessage());
                        }
                        return fileData;
                    })
                    .collect(Collectors.toList());
        }

        Review review = dtoModifier.getReviewByEditReviewForm(editReviewForm);
        review.setImageFiles(fileDataList);
        Integer resultCount = reviewSVC.update(rvno, review, editReviewForm.getDeleteImages());
        facilitySVC.updateToScore(reviewSVC.findByRvno(rvno).getFcno());

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("resultCount", resultCount)
                .setData("redirect", "/facilities/" + reviewSVC.findByRvno(rvno).getFcno());

        return new ResponseEntity(responseMsg, HttpStatus.OK);
    }

    //리뷰 삭제 처리
    @DeleteMapping("/{rvno}")
    public ResponseEntity<ResponseMsg> delete(@PathVariable Long rvno) {
        Long fcno = reviewSVC.findByRvno(rvno).getFcno();
        Integer resultCount = reviewSVC.delete(rvno);
        facilitySVC.updateToScore(fcno);

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("resultCount",resultCount);
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
}
