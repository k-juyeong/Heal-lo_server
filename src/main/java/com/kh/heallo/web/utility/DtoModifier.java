package com.kh.heallo.web.utility;

import com.kh.heallo.domain.facility.AutoComplete;
import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.FacilityCriteria;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.ReviewCriteria;
import com.kh.heallo.domain.uploadfile.FileData;
import com.kh.heallo.web.facility.dto.AutoCompleteDto;
import com.kh.heallo.web.facility.dto.FacilityCriteriaDto;
import com.kh.heallo.web.facility.dto.FacilitySearchDto;
import com.kh.heallo.web.review.dto.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoModifier {

    //SearchCriteria => FacilityCriteria
    public FacilityCriteria getFacilityCriteria(FacilityCriteriaDto facilityCriteriaDto) {
        FacilityCriteria criteria = new FacilityCriteria();
        BeanUtils.copyProperties(facilityCriteriaDto,criteria);
        criteria.setFcaddr(criteria.getFcaddr()+"%");
        criteria.setFcname(criteria.getFcname()+"%");
        criteria.setFctype("%"+ criteria.getFctype()+"%");

        return criteria;
    }

    //Facility => facilityDto
    public FacilitySearchDto getFacilityDto(Facility facility) {
        FacilitySearchDto facilityDto = new FacilitySearchDto();
        BeanUtils.copyProperties(facility,facilityDto);

        return facilityDto;
    }

    //SearchCriteria => ReviewCriteria
    public ReviewCriteria getReviewCriteria(ReviewCriteriaDto reviewCriteriaDto) {
        ReviewCriteria reviewCriteria = new ReviewCriteria();
        BeanUtils.copyProperties(reviewCriteriaDto,reviewCriteria);

        return reviewCriteria;
    }

    //List<FileData> => List<ReviewFileData>
    public List<ReviewFileData> getReviewFileDataList(List<FileData> imageFiles) {
        List<ReviewFileData> reviewFileDataList = imageFiles.stream().map(fileData -> {
            ReviewFileData reviewFileData = new ReviewFileData();
            BeanUtils.copyProperties(fileData, reviewFileData);

            return reviewFileData;
        }).collect(Collectors.toList());
        return reviewFileDataList;
    }

    //Review => ReviewDto
    public ReviewDto getReviewDto(Review review, Long memno) {
        ReviewDto reviewDto = new ReviewDto();
        BeanUtils.copyProperties(review,reviewDto);
        reviewDto.setMemnickname(review.getMember().getMemnickname());

        if (review.getMemno() == memno) reviewDto.setLogin(true);

        if(review.getImageFiles() != null) {
            List<FileData> imageFiles = review.getImageFiles();
            List<ReviewFileData> reviewFileDataList = getReviewFileDataList(imageFiles);
            reviewDto.setImageFiles(reviewFileDataList);
        }

        return reviewDto;
    }

    //List<AutoComplete> => List<AutoCompleteDto>
    public List<AutoCompleteDto> getAutoCompleteDto(List<AutoComplete> autoCompleteList) {
        List<AutoCompleteDto> completeDtos = autoCompleteList.stream().map(autoComplete -> {
            AutoCompleteDto autoCompleteDto = new AutoCompleteDto();
            BeanUtils.copyProperties(autoComplete, autoCompleteDto);

            return autoCompleteDto;
        }).collect(Collectors.toList());
        return completeDtos;
    }

    //EditReviewForm => Review
    public Review getReviewByEditReviewForm(EditReviewForm editReviewForm) {
        Review review = new Review();
        BeanUtils.copyProperties(editReviewForm, review);

        return review;
    }

    //AddReviewForm => Review
    public Review getReviewByAddReviewForm(AddReviewForm addReviewForm) {
        Review review = new Review();
        BeanUtils.copyProperties(addReviewForm, review);

        return review;
    }

    //Review => EditReviewForm
    public EditReviewForm getEditReviewFormByReview(Review review) {
        EditReviewForm editReviewForm = new EditReviewForm();
        BeanUtils.copyProperties(review, editReviewForm);

        return editReviewForm;
    }

    //List<MultipartFile> => List<ReviewFileData>
    public List<ReviewFileData> getReviewFileData(List<MultipartFile> multipartFiles) {
        List<ReviewFileData> reviewFileDataList = multipartFiles.stream().map(multipartFile -> {
            ReviewFileData reviewFileData = new ReviewFileData();
            reviewFileData.setUffname(multipartFile.getOriginalFilename());
            reviewFileData.setUftype(multipartFile.getContentType());
            reviewFileData.setUfsize(multipartFile.getSize());
            return reviewFileData;
        }).collect(Collectors.toList());
        return reviewFileDataList;
    }
}
