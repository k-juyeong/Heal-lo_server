package com.kh.heallo.web;

import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.FacilityCriteria;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.ReviewCriteria;
import com.kh.heallo.web.facility.dto.FacilityCriteriaDto;
import com.kh.heallo.web.facility.dto.FacilityDto;
import com.kh.heallo.web.review.dto.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoModifier {

    //SearchCriteria => FacilityCriteria
    public FacilityCriteria getFacilityCriteria(FacilityCriteriaDto facilityCriteriaDto) {
        FacilityCriteria criteria = new FacilityCriteria();
        BeanUtils.copyProperties(facilityCriteriaDto,criteria);

        return criteria;
    }

    //Review => ReviewDto
    public ReviewDto getReviewDto(Review review, Long memno) {
        ReviewDto reviewDto = new ReviewDto();
        BeanUtils.copyProperties(review,reviewDto);
        reviewDto.setMemno(review.getMemno());

        if (review.getMemno() == memno) reviewDto.setLogin(true);

        if(review.getImageFiles() != null) {
            List<ReviewFileData> reviewFileDataList = review.getImageFiles().stream().map(fileData -> {
                ReviewFileData reviewFileData = new ReviewFileData();
                BeanUtils.copyProperties(fileData, reviewFileData);

                return reviewFileData;
            }).collect(Collectors.toList());
            reviewDto.setImageFiles(reviewFileDataList);
        }

        return reviewDto;
    }

    //SearchCriteria => ReviewCriteria
    public ReviewCriteria getReviewCriteria(ReviewCriteriaDto reviewCriteriaDto) {
        ReviewCriteria reviewCriteria = new ReviewCriteria();
        BeanUtils.copyProperties(reviewCriteriaDto,reviewCriteria);

        return reviewCriteria;
    }

    //Facility => facilityDto
    public FacilityDto getFacilityDto(Facility facility) {
        FacilityDto facilityDto = new FacilityDto();
        BeanUtils.copyProperties(facility,facilityDto);

        return facilityDto;
    }

    //EditReviewForm => Review
    public Review getReviewByEditReviewForm(EditReviewForm editReviewForm) {
        Review review = new Review();
        BeanUtils.copyProperties(editReviewForm, review);

        return review;
    }

    //EditReviewForm => Review
    public EditReviewForm getEditReviewFormByReview(Review review) {
        EditReviewForm editReviewForm = new EditReviewForm();
        BeanUtils.copyProperties(review, editReviewForm);



        return editReviewForm;
    }

    //AddReviewForm => Review
    public Review getReviewByAddReviewForm(AddReviewForm addReviewForm) {
        Review review = new Review();
        BeanUtils.copyProperties(addReviewForm, review);

        return review;
    }
}
