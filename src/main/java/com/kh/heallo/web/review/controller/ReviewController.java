package com.kh.heallo.web.review.controller;

import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.domain.file.FileData;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.ReviewCriteria;
import com.kh.heallo.domain.review.svc.ReviewSVC;
import com.kh.heallo.web.ResponseMsg;
import com.kh.heallo.web.facility.dto.FacilityDto;
import com.kh.heallo.web.FileSetting;
import com.kh.heallo.web.DtoModifier;
import com.kh.heallo.web.review.dto.AddReviewForm;
import com.kh.heallo.web.review.dto.SearchCriteria;
import com.kh.heallo.web.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewSVC reviewSVC;
    private final FacilitySVC facilitySVC;
    private final FileSetting fileSetting;
    private final DtoModifier dtoModifier;

    //리뷰 전체 갯수 조회
    @ResponseBody
    @GetMapping("/{fcno}/total")
    public ResponseEntity<ResponseMsg> getTotalCount(@PathVariable("fcno") Long fcno) {

        //Get totalCount
        Integer totalCount = reviewSVC.getTotalCount(fcno);
        Map<String, Object> data = new HashMap<>();
        data.put("totalCount", totalCount);

        //Create ResponseEntity
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseMsg responseMsg = new ResponseMsg("OK","성공",data);

        return new ResponseEntity<>(responseMsg, headers, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/{fcno}/list")
    public ResponseEntity<ResponseMsg> findListByFcno(
            @PathVariable("fcno") Long fcno,
            @ModelAttribute SearchCriteria searchCriteria
    ) {

        //SearchCriteria => ReviewCriteria
        ReviewCriteria reviewCriteria = dtoModifier.getReviewCriteria(searchCriteria);

        //Get List<Review>
        List<Review> FoundReviewList = reviewSVC.findListByFcno(fcno, reviewCriteria);

        //List<Review> => List<ReviewDto>
        List<ReviewDto> reviewList = dtoModifier.getReviewDtos(FoundReviewList);

        //Create ResponseEntity
        Map<String, Object> data = new HashMap<>();
        data.put("reviews", reviewList);
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseMsg responseMsg = new ResponseMsg("OK","성공", data);

        return new ResponseEntity<>(responseMsg,headers,HttpStatus.OK);
    }

    @GetMapping("/{fcno}/add")
    public String addForm(@PathVariable("fcno") Long fcno, Model model) {
        Facility foundFacility = facilitySVC.findByFcno(fcno);

        //Facility => facilityDto
        FacilityDto facilityDto = dtoModifier.getFacilityDto(foundFacility);

        //model (facilityDto,AddReviewForm)
        model.addAttribute("facility",facilityDto);
        model.addAttribute("reviewForm", new AddReviewForm());

        return "/review/review-write";
    }

    @ResponseBody
    @PostMapping("/{fcno}/add")
    public ResponseEntity add(
            @PathVariable("fcno") Long fcno,
            @ModelAttribute("reviewForm") AddReviewForm addReviewForm
    ) {
        long memno = 1L; //임시 회원

        log.info("addReviewForm {}",addReviewForm);

        //AddReviewForm => Review
        Review review = dtoModifier.getReview(fcno, addReviewForm, memno);

        //MultipartFile => FileData
        if(addReviewForm.getAttachedImage() != null) {
            List<MultipartFile> imageFiles = addReviewForm.getAttachedImage();
            List<FileData> fileDataList = fileSetting.getFileDataList(imageFiles);
            review.setAttachedImage(fileDataList);
        }

        //Add Review
        reviewSVC.add(1L, fcno, review);

        //Create ResponseEntity
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        headers.setLocation(URI.create("/facilities/" + fcno));

        return new ResponseEntity(headers,HttpStatus.OK);
    }
}
