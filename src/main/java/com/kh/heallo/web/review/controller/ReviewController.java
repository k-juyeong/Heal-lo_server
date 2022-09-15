package com.kh.heallo.web.review.controller;

import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.domain.uploadfile.FileData;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.ReviewCriteria;
import com.kh.heallo.domain.review.svc.ReviewSVC;
import com.kh.heallo.web.FieldErrorDetail;
import com.kh.heallo.web.ResponseMsg;
import com.kh.heallo.web.StatusCode;
import com.kh.heallo.web.facility.dto.FacilityDto;
import com.kh.heallo.web.utility.FileSetting;
import com.kh.heallo.web.utility.DtoModifier;
import com.kh.heallo.web.review.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewSVC reviewSVC;
    private final FacilitySVC facilitySVC;
    private final FileSetting fileSetting;
    private final DtoModifier dtoModifier;

    //리뷰 total/age 조회
    @ResponseBody
    @GetMapping("/{fcno}/total")
    public ResponseEntity<ResponseMsg> getTotalCount(@PathVariable("fcno") Long fcno) {
        Integer totalCount = reviewSVC.getTotalCount(fcno);
        double fcscore = facilitySVC.findByFcno(fcno).getFcscore();

        //Create ResponseEntity
        String message = totalCount > 0 ? "해당 운동시설에 대한 전체 리뷰 수를 조회했습니다." : "검색결과가 없습니다.";
        ResponseMsg responseMsg = new ResponseMsg()
                .setStatusCode(StatusCode.SUCCESS)
                .setMessage(message)
                .setData("totalCount", totalCount)
                .setData("fcscore", fcscore);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    //해당 운동시설 리뷰 리스트 조회(페이징)
    @ResponseBody
    @GetMapping("/{fcno}/list")
    public ResponseEntity<ResponseMsg> findListByFcno(
            @PathVariable("fcno") Long fcno,
            @ModelAttribute ReviewCriteriaDto searchCriteria
    ) {
        //Test 회원
        long memno = 1L;

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
                .setStatusCode(StatusCode.SUCCESS)
                .setMessage("해당 운동시설에 대한 리뷰를 조회했습니다.")
                .setData("reviews", reviewDtos);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    //리뷰 등록 폼 이동
    @GetMapping("/{fcno}/add")
    public String addForm(
            @PathVariable("fcno") Long fcno,
            Model model
    ) {
        FacilityDto facilityDto = dtoModifier.getFacilityDto(facilitySVC.findByFcno(fcno));
        model.addAttribute("facility", facilityDto);

        return "/review/review-write";
    }

    //리뷰 등록 처리
    @ResponseBody
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
            BindException bindException = new BindException(addReviewForm, "addReviewForm");
            List<ReviewFileData> reviewFileDataList = dtoModifier.getReviewFileData(multipartFiles);
            addReviewForm.setImageFiles(reviewFileDataList);
            fileValidate(bindException, reviewFileDataList, reviewFileDataList.size());

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
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/facilities/" + fcno));
        ResponseMsg responseMsg = new ResponseMsg()
                .setStatusCode(StatusCode.SUCCESS)
                .setMessage("리뷰 등록에 성공했습니다")
                .setData("rvno", rvno);

        return new ResponseEntity<>(responseMsg, headers, HttpStatus.OK);
    }


    //리뷰 수정 폼 이동
    @GetMapping("/{rvno}/edit")
    public String updateForm(@PathVariable Long rvno, Model model) {
        Review foundReview = reviewSVC.findByRvno(rvno);
        EditReviewForm editReviewForm = dtoModifier.getEditReviewFormByReview(foundReview);

        List<ReviewFileData> reviewFileDataList = foundReview
                .getImageFiles()
                .stream()
                .map(fileData -> {
                    ReviewFileData reviewFileData = new ReviewFileData();
                    BeanUtils.copyProperties(fileData, reviewFileData);
                    return reviewFileData;
                })
                .collect(Collectors.toList());

        editReviewForm.setImageFiles(reviewFileDataList);
        model.addAttribute("reviewForm", editReviewForm);

        FacilityDto facilityDto = dtoModifier.getFacilityDto(facilitySVC.findByFcno(foundReview.getFcno()));
        model.addAttribute("facility", facilityDto);

        return "/review/review-update";
    }

    //리뷰 수정 처리
    @ResponseBody
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
            BindException bindException = new BindException(editReviewForm, "addReviewForm");
            List<ReviewFileData> reviewFileDataList = dtoModifier.getReviewFileData(multipartFiles);
            Review review = reviewSVC.findByRvno(rvno);
            int totalSize = review.getImageFiles().size()
                    - editReviewForm.getDeleteImages().length
                    + reviewFileDataList.size();
            editReviewForm.setImageFiles(reviewFileDataList);
            fileValidate(bindException, reviewFileDataList , totalSize);

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
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/facilities/" + reviewSVC.findByRvno(rvno).getFcno()));
        ResponseMsg responseMsg = new ResponseMsg()
                .setStatusCode(StatusCode.SUCCESS)
                .setMessage("리뷰를 성공적으로 수정했습니다.")
                .setData("resultCount", resultCount);

        return new ResponseEntity(responseMsg, headers, HttpStatus.OK);
    }

    //리뷰 삭제 처리
    @ResponseBody
    @DeleteMapping("/{rvno}")
    public ResponseEntity<ResponseMsg> delete(@PathVariable Long rvno) {
        Integer resultCount = reviewSVC.delete(rvno);
        facilitySVC.updateToScore(reviewSVC.findByRvno(rvno).getFcno());

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .setStatusCode(StatusCode.SUCCESS)
                .setMessage("리뷰를 성공적으로 삭제했습니다.")
                .setData("resultCount",resultCount);
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    private void fileValidate(BindException bindException, List<ReviewFileData> reviewFileDataList, int totalSize) throws BindException {
        if(totalSize > 5) {
            bindException.rejectValue("imageFiles","imageFiles.size",new Object[]{"5"},"파일 업로드 오류");
        }
        int NotSupportTypeLength = reviewFileDataList
                .stream()
                .filter(multipartFile -> !multipartFile.getUftype().equals("image/jpeg") && !multipartFile.getUftype().equals("image/png"))
                .collect(Collectors.toList()).size();
        if (NotSupportTypeLength > 0) {
            bindException.rejectValue("imageFiles","imageFiles.type","파일 업로드 오류");
        }
        if (bindException.hasErrors()) throw bindException;
    }
}
