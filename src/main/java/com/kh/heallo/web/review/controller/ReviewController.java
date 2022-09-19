package com.kh.heallo.web.review.controller;

import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.svc.ReviewSVC;
import com.kh.heallo.web.facility.dto.FacilitySearchDto;
import com.kh.heallo.web.review.dto.EditReviewForm;
import com.kh.heallo.web.review.dto.ReviewFileData;
import com.kh.heallo.web.utility.DtoModifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final DtoModifier dtoModifier;
    private final FacilitySVC facilitySVC;
    private final ReviewSVC reviewSVC;

    //리뷰 등록 폼 이동
    @GetMapping("/{fcno}/add")
    public String addForm(
            @PathVariable("fcno") Long fcno,
            Model model
    ) {

        FacilitySearchDto facilityDto = dtoModifier.getFacilityDto(facilitySVC.findByFcno(fcno));
        model.addAttribute("facility", facilityDto);

        return "/review/review-write";
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

        FacilitySearchDto facilityDto = dtoModifier.getFacilityDto(facilitySVC.findByFcno(foundReview.getFcno()));
        model.addAttribute("facility", facilityDto);

        return "/review/review-update";
    }

}
