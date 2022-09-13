package com.kh.heallo.web.review.controller;

import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.domain.uploadfile.FileData;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.ReviewCriteria;
import com.kh.heallo.domain.review.svc.ReviewSVC;
import com.kh.heallo.web.FieldErrorDetail;
import com.kh.heallo.web.ResponseMsg;
import com.kh.heallo.web.facility.dto.FacilityDto;
import com.kh.heallo.web.FileSetting;
import com.kh.heallo.web.DtoModifier;
import com.kh.heallo.web.review.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

    //리뷰 전체 갯수 조회
    @ResponseBody
    @GetMapping("/{fcno}/total")
    public ResponseEntity<ResponseMsg> getTotalCount(@PathVariable("fcno") Long fcno) {
        //Get totalCount
        Integer totalCount = reviewSVC.getTotalCount(fcno);
        Map<String, Object> data = new HashMap<>();
        data.put("totalCount", totalCount);

        //Create ResponseEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseMsg responseMsg = new ResponseMsg("OK", "성공", data);

        return new ResponseEntity<>(responseMsg, headers, HttpStatus.OK);
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

        //SearchCriteria => ReviewCriteria
        ReviewCriteria reviewCriteria = dtoModifier.getReviewCriteria(searchCriteria);

        //Get List<Review>
        List<Review> foundReviewList = reviewSVC.findListByFcno(fcno, reviewCriteria);
        log.info("reviewList {}", foundReviewList);

        //List<Review> => List<ReviewDto>
        List<ReviewDto> reviewDtos = foundReviewList.stream().map(review -> {
            ReviewDto reviewDto = dtoModifier.getReviewDto(review, memno);
            return reviewDto;
        }).collect(Collectors.toList());
        log.info("reviewDtos {}", reviewDtos);

        //Create ResponseEntity
        Map<String, Object> data = new HashMap<>();
        data.put("reviews", reviewDtos);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseMsg responseMsg = new ResponseMsg("OK", "성공", data);

        return new ResponseEntity<>(responseMsg, headers, HttpStatus.OK);
    }

    //리뷰 등록 폼 이동
    @GetMapping("/{fcno}/add")
    public String addForm(
            @PathVariable("fcno") Long fcno,
            Model model
    ) {
        //Get Facility
        Facility foundFacility = facilitySVC.findByFcno(fcno);

        //Facility => facilityDto
        FacilityDto facilityDto = dtoModifier.getFacilityDto(foundFacility);

        //model addAttribute
        model.addAttribute("facility", facilityDto);
        model.addAttribute("reviewForm", new AddReviewForm());

        return "/review/review-write";
    }

    //리뷰 등록 처리
    @ResponseBody
    @PostMapping("/{fcno}/add")
    public ResponseEntity add(
            @ModelAttribute("reviewForm") AddReviewForm addReviewForm,
            @PathVariable("fcno") Long fcno,
            List<MultipartFile> multipartFiles
    ) {
        //Test 회원
        long memno = 1L;

        //AddReviewForm => Review
        Review review = dtoModifier.getReviewByAddReviewForm(addReviewForm);
        review.setFcno(fcno);
        review.setMemno(memno);

        //MultipartFile => FileData
        if (multipartFiles != null) {
            List<FileData> fileDataList = multipartFiles.stream().map(multipartFile -> {
                log.info("multipart {}",multipartFile);
                FileData fileData = null;
                try {
                    String localFileName = fileSetting.transForTo(multipartFile);
                    fileData = fileSetting.getFileData(multipartFile, localFileName);
                } catch (IOException e) {
                    log.info("파일 저장중 exception 발생 {}", e.getMessage());
                }
                log.info("fileData {}",fileData);

                return fileData;
            }).collect(Collectors.toList());
            review.setImageFiles(fileDataList);
        }

        //Add Review
        reviewSVC.add(memno, fcno, review);

        //Update Facility By Fcscore
        facilitySVC.updateToScore(fcno);

        //Create ResponseEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        headers.setLocation(URI.create("/facilities/" + fcno));

        return new ResponseEntity(headers, HttpStatus.OK);
    }

    //리뷰 수정 폼 이동
    @GetMapping("/{rvno}/edit")
    public String updateForm(@PathVariable Long rvno, Model model) {
        //Get Review
        Review foundReview = reviewSVC.findByRvno(rvno);

        //Review => EditReviewForm
        EditReviewForm editReviewForm = dtoModifier.getEditReviewFormByReview(foundReview);

        log.info("foundReview.getImageFiles() {}",foundReview.getImageFiles());

        //FileData => ReviewFileData
        List<ReviewFileData> reviewFileDataList = foundReview.getImageFiles().stream().map(fileData -> {
            ReviewFileData reviewFileData = new ReviewFileData();
            BeanUtils.copyProperties(fileData, reviewFileData);
            return reviewFileData;
        }).collect(Collectors.toList());
        editReviewForm.setImageFiles(reviewFileDataList);

        //Get Facility
        Facility foundFacility = facilitySVC.findByFcno(foundReview.getFcno());

        //Facility => FacilityDto
        FacilityDto facilityDto = dtoModifier.getFacilityDto(foundFacility);

        //model addAttribute
        model.addAttribute("reviewForm", editReviewForm);
        model.addAttribute("facility", facilityDto);

        return "/review/review-update";
    }

    //리뷰 수정 처리
    @ResponseBody
    @PatchMapping("/{rvno}/edit")
    public ResponseEntity<ResponseMsg> update(
            @PathVariable("rvno") Long rvno,
            @Valid @ModelAttribute("reviewForm") EditReviewForm editReviewForm,
            List<MultipartFile> multipartFiles,
            BindingResult bindingResult,
            Locale locale
    ) {

        //실패
        if (bindingResult.hasErrors()) {
            List<FieldErrorDetail> fieldErrors = bindingResult.getFieldErrors().stream().map(fieldError -> {
                FieldErrorDetail fieldErrorDetail = FieldErrorDetail.create(
                        fieldError,
                        messageSource,
                        locale);
                return fieldErrorDetail;
            }).collect(Collectors.toList());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            return new ResponseEntity(fieldErrors, headers, HttpStatus.BAD_REQUEST);
        }

        //성공
        //Get Fcno
        Long fcno = reviewSVC.findByRvno(rvno).getFcno();

        //EditReviewForm => Review
        Review review = dtoModifier.getReviewByEditReviewForm(editReviewForm);

        //MultipartFile => FileData
        if (multipartFiles != null) {
            List<FileData> fileDataList = multipartFiles.stream().map(multipartFile -> {
                log.info("multipart {}",multipartFile);
                FileData fileData = null;
                try {
                    String localFileName = fileSetting.transForTo(multipartFile);
                    fileData = fileSetting.getFileData(multipartFile, localFileName);
                } catch (IOException e) {
                    log.info("파일 저장중 exception 발생 {}", e.getMessage());
                }
                log.info("fileData {}",fileData);

                return fileData;
            }).collect(Collectors.toList());
            review.setImageFiles(fileDataList);
        }

        //Update Review
        Integer resultCount = reviewSVC.update(rvno, review);

        //Update Facility By Fcscore
        facilitySVC.updateToScore(fcno);

        //Create ResponseEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        headers.setLocation(URI.create("/facilities/" + fcno));

        return new ResponseEntity(headers, HttpStatus.OK);
    }

    //리뷰 삭제 처리
    @ResponseBody
    @DeleteMapping("/{rvno}")
    public ResponseEntity<ResponseMsg> delete(@PathVariable Long rvno) {
        //Get Fcno
        Long fcno = reviewSVC.findByRvno(rvno).getFcno();

        //Delete Review
        Integer resultCount = reviewSVC.delete(rvno);

        //Update Facility By Fcscore
        facilitySVC.updateToScore(fcno);

        //Create ResponseEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseMsg responseMsg = new ResponseMsg("OK", "성공", resultCount);
        return new ResponseEntity<>(responseMsg, headers, HttpStatus.OK);
    }
}
