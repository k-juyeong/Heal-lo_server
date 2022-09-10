package com.kh.heallo.web.review.controller;

import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.domain.uploadfile.FileData;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.ReviewCriteria;
import com.kh.heallo.domain.review.svc.ReviewSVC;
import com.kh.heallo.domain.uploadfile.svc.UploadFileSVC;
import com.kh.heallo.web.ResponseMsg;
import com.kh.heallo.web.facility.dto.FacilityDto;
import com.kh.heallo.web.FileSetting;
import com.kh.heallo.web.DtoModifier;
import com.kh.heallo.web.review.dto.AddReviewForm;
import com.kh.heallo.web.review.dto.SearchCriteria;
import com.kh.heallo.web.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
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

    //해당 운동시설 리뷰 리스트 조회(페이징)
    @ResponseBody
    @GetMapping("/{fcno}/list")
    public ResponseEntity<ResponseMsg> findListByFcno(
            @PathVariable("fcno") Long fcno,
            @ModelAttribute SearchCriteria searchCriteria
    ) {
        //Test 회원
        long memno = 3L;

        //SearchCriteria => ReviewCriteria
        ReviewCriteria reviewCriteria = dtoModifier.getReviewCriteria(searchCriteria);

        //Get List<Review>
        List<Review> FoundReviewList = reviewSVC.findListByFcno(fcno, reviewCriteria);

        //List<Review> => List<ReviewDto>
        List<ReviewDto> reviewList = dtoModifier.getReviewDtos(FoundReviewList,memno);

        //Create ResponseEntity
        Map<String, Object> data = new HashMap<>();
        data.put("reviews", reviewList);
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseMsg responseMsg = new ResponseMsg("OK","성공", data);

        return new ResponseEntity<>(responseMsg,headers,HttpStatus.OK);
    }

    //이미지 연결
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileSetting.getFullPath(filename));
    }

    //리뷰 등록 폼 이동
    @GetMapping("/{fcno}/add")
    public String addForm(@PathVariable("fcno") Long fcno, Model model) {
        //Get Facility
        Facility foundFacility = facilitySVC.findByFcno(fcno);

        //Facility => facilityDto
        FacilityDto facilityDto = dtoModifier.getFacilityDto(foundFacility);

        //model addAttribute
        model.addAttribute("facility",facilityDto);
        model.addAttribute("reviewForm", new AddReviewForm());

        return "/review/review-write";
    }

    //리뷰 등록 처리
    @ResponseBody
    @PostMapping("/{fcno}/add")
    public ResponseEntity add(
            @PathVariable("fcno") Long fcno,
            @ModelAttribute("reviewForm") AddReviewForm addReviewForm
    ) {
        //Test 회원
        long memno = 3L;

        //AddReviewForm => Review
        Review review = dtoModifier.getReview(addReviewForm);
        review.setFcno(fcno);
        review.setMemno(memno);

        //MultipartFile => FileData
        if(addReviewForm.getAttachedImage() != null) {
            List<MultipartFile> imageFiles = addReviewForm.getAttachedImage();
            List<FileData> fileDataList = imageFiles.stream().map(multipartFile -> {
                FileData fileData = null;
                try {
                    String localFileName = fileSetting.transForTo(multipartFile);
                    fileData = fileSetting.getFileData(multipartFile, localFileName);
                } catch (IOException e) {
                    log.info("파일 저장중 exception 발생 {}", e.getMessage());
                }

                return fileData;
            }).collect(Collectors.toList());
            review.setAttachedImage(fileDataList);
        }

        //Add Review
        reviewSVC.add(memno, fcno, review);

        //Update Facility By Fcscore
        facilitySVC.updateToScore(fcno);

        //Create ResponseEntity
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        headers.setLocation(URI.create("/facilities/" + fcno));

        return new ResponseEntity(headers,HttpStatus.OK);
    }

}
