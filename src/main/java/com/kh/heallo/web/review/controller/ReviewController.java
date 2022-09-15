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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    private final MessageSource messageSource;

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

        ReviewCriteria reviewCriteria = dtoModifier.getReviewCriteria(searchCriteria);
        List<Review> reviewList = reviewSVC.findListByFcno(fcno, reviewCriteria);
        List<ReviewDto> reviewDtos = reviewList.stream().map(review -> {
            ReviewDto reviewDto = dtoModifier.getReviewDto(review, memno);
            return reviewDto;
        }).collect(Collectors.toList());

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
        Facility foundFacility = facilitySVC.findByFcno(fcno);
        FacilityDto facilityDto = dtoModifier.getFacilityDto(foundFacility);
        model.addAttribute("facility", facilityDto);

        return "/review/review-write";
    }

    //리뷰 등록 처리
    @ResponseBody
    @PostMapping("/{fcno}")
    public ResponseEntity add(
            @PathVariable("fcno") Long fcno,
            @Valid @ModelAttribute AddReviewForm addReviewForm,
            BindingResult bindingResult,
            Locale locale
    ) {
        //Test 회원
        long memno = 1L;

        //업로드 파일 검증
        if (addReviewForm.getMultipartFiles() != null) {
            List<ReviewFileData> reviewFileDataList = dtoModifier.getReviewFileData(addReviewForm.getMultipartFiles());
            addReviewForm.setImageFiles(reviewFileDataList);

            if(reviewFileDataList.size() > 5) {
                bindingResult.rejectValue("imageFiles","imageFiles.size",new Object[]{"5"},"파일 업로드 오류");
            }

            int NotSupportTypeLength = reviewFileDataList
                    .stream()
                    .filter(multipartFile -> !multipartFile.getUftype().equals("image/jpeg") && !multipartFile.getUftype().equals("image/png"))
                    .collect(Collectors.toList()).size();

            if (NotSupportTypeLength > 0) {
                bindingResult.rejectValue("imageFiles","imageFiles.type","파일 업로드 오류");
            }
        }

        //검증오류
        if (bindingResult.hasErrors()) {
            List<FieldErrorDetail> errorDetails = bindingResult.getFieldErrors().stream().map(fieldError -> {
                FieldErrorDetail fieldErrorDetail = FieldErrorDetail.create(fieldError, messageSource, locale);
                return fieldErrorDetail;
            }).collect(Collectors.toList());

            //Create ResponseEntity
            ResponseMsg responseMsg = new ResponseMsg()
                    .setStatusCode(StatusCode.VALIDATION_ERROR)
                    .setMessage("리뷰 등록에 실패했습니다. 값을 확인해주세요")
                    .setData("errors", errorDetails);

            return new ResponseEntity<>(responseMsg, HttpStatus.BAD_REQUEST);
        }

        //성공
        Review review = dtoModifier.getReviewByAddReviewForm(addReviewForm);
        review.setFcno(fcno);
        review.setMemno(memno);

        if (addReviewForm.getMultipartFiles() != null) {
            List<FileData> fileDataList = addReviewForm.getMultipartFiles().stream().map(multipartFile -> {

                FileData fileData = null;
                try {
                    String localFileName = fileSetting.transForTo(multipartFile);
                    fileData = fileSetting.getFileData(multipartFile, localFileName);
                } catch (IOException e) {
                    log.info("파일 저장중 exception 발생 {}", e.getMessage());
                }

                return fileData;
            }).collect(Collectors.toList());
            review.setImageFiles(fileDataList);
        }

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

        List<ReviewFileData> reviewFileDataList = foundReview.getImageFiles().stream().map(fileData -> {
            ReviewFileData reviewFileData = new ReviewFileData();
            BeanUtils.copyProperties(fileData, reviewFileData);
            return reviewFileData;
        }).collect(Collectors.toList());
        editReviewForm.setImageFiles(reviewFileDataList);
        model.addAttribute("reviewForm", editReviewForm);

        Facility foundFacility = facilitySVC.findByFcno(foundReview.getFcno());
        FacilityDto facilityDto = dtoModifier.getFacilityDto(foundFacility);
        model.addAttribute("facility", facilityDto);

        return "/review/review-update";
    }

    //리뷰 수정 처리
    @ResponseBody
    @PatchMapping("/{rvno}")
    public ResponseEntity<ResponseMsg> update(
            @PathVariable("rvno") Long rvno,
            @Valid @ModelAttribute EditReviewForm editReviewForm,
            BindingResult bindingResult,
            Locale locale
    ) {
        //Test 회원
        long memno = 1L;

        //업로드 파일 검증
        if (editReviewForm.getMultipartFiles() != null) {
            List<ReviewFileData> reviewFileDataList = dtoModifier.getReviewFileData(editReviewForm.getMultipartFiles());
            editReviewForm.setImageFiles(reviewFileDataList);

            if (reviewFileDataList != null && reviewFileDataList.size() > 5) {
                bindingResult.rejectValue("imageFiles", "imageFiles.size", new Object[]{"5"}, "파일 업로드 오류");
            }

            int NotSupportTypeLength = reviewFileDataList
                    .stream()
                    .filter(multipartFile -> !multipartFile.getUftype().equals("image/jpeg") && !multipartFile.getUftype().equals("image/png"))
                    .collect(Collectors.toList()).size();

            if (NotSupportTypeLength > 0) {
                bindingResult.rejectValue("imageFiles", "imageFiles.type", "파일 업로드 오류");
            }
        }

        //검증오류
        if (bindingResult.hasErrors()) {
            List<FieldErrorDetail> errorDetails = bindingResult.getFieldErrors().stream().map(fieldError -> {
                FieldErrorDetail fieldErrorDetail = FieldErrorDetail.create(fieldError, messageSource, locale);
                return fieldErrorDetail;
            }).collect(Collectors.toList());

            //Create ResponseEntity
            ResponseMsg responseMsg = new ResponseMsg()
                    .setStatusCode(StatusCode.VALIDATION_ERROR)
                    .setMessage("리뷰 수정에 실패했습니다. 값을 확인해주세요")
                    .setData("errors", errorDetails);

            return new ResponseEntity<>(responseMsg, HttpStatus.BAD_REQUEST);
        }

        //성공
        Long fcno = reviewSVC.findByRvno(rvno).getFcno();
        Review review = dtoModifier.getReviewByEditReviewForm(editReviewForm);

        if (editReviewForm.getMultipartFiles() != null) {
            List<FileData> fileDataList = editReviewForm.getMultipartFiles().stream().map(multipartFile -> {

                FileData fileData = null;
                try {
                    String localFileName = fileSetting.transForTo(multipartFile);
                    fileData = fileSetting.getFileData(multipartFile, localFileName);
                } catch (IOException e) {
                    log.info("파일 저장중 exception 발생 {}", e.getMessage());
                }

                return fileData;
            }).collect(Collectors.toList());
            review.setImageFiles(fileDataList);
        }

        Integer resultCount = reviewSVC.update(rvno, review);
        facilitySVC.updateToScore(fcno);

        //Create ResponseEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/facilities/" + fcno));
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
        Long fcno = reviewSVC.findByRvno(rvno).getFcno();
        Integer resultCount = reviewSVC.delete(rvno);
        facilitySVC.updateToScore(fcno);

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .setStatusCode(StatusCode.SUCCESS)
                .setMessage("리뷰를 성공적으로 삭제했습니다.");
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
}
