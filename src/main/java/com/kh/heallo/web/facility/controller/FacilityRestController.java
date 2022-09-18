package com.kh.heallo.web.facility.controller;

import com.kh.heallo.domain.facility.FacilityCriteria;
import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.domain.review.svc.ReviewSVC;
import com.kh.heallo.web.StatusCode;
import com.kh.heallo.web.facility.dto.FacilitySearchDto;
import com.kh.heallo.web.utility.DtoModifier;
import com.kh.heallo.web.ResponseMsg;
import com.kh.heallo.web.facility.dto.FacilityCriteriaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/facilities")
@RequiredArgsConstructor
@Slf4j
public class FacilityRestController {

    private final FacilitySVC facilitySVC;
    private final ReviewSVC reviewSVC;
    private final DtoModifier dtoModifier;

    //검색 결과 수
    @ResponseBody
    @GetMapping("/total")
    public ResponseEntity<ResponseMsg> totalCount(@ModelAttribute FacilityCriteriaDto searchCriteria) {
        FacilityCriteria facilityCriteria = dtoModifier.getFacilityCriteria(searchCriteria);
        Integer totalCount = facilitySVC.getTotalCount(facilityCriteria);

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("totalCount", totalCount);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    //검색 결과(페이징) **
    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity<ResponseMsg> search(@ModelAttribute FacilityCriteriaDto searchCriteria) {
        FacilityCriteria criteria = dtoModifier.getFacilityCriteria(searchCriteria);
        List<Facility> facilityList = facilitySVC.search(criteria);
        List<FacilitySearchDto> facilitySearchDtos = facilityList.stream().map(facility -> {
            FacilitySearchDto facilitySearchDto = new FacilitySearchDto();
            BeanUtils.copyProperties(facility, facilitySearchDto);
            facilitySearchDto.setRvtotal(reviewSVC.getTotalCount(facility.getFcno()));

            return facilitySearchDto;
        }).collect(Collectors.toList());

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("facilities", facilitySearchDtos);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    //운동시설 평균 조회
    @ResponseBody
    @GetMapping("/{fcno}/score")
    public ResponseEntity<ResponseMsg> getScore(@PathVariable("fcno") Long fcno) {
        double fcScore = facilitySVC.findByFcno(fcno).getFcscore();

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("fcScore", fcScore);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
}
