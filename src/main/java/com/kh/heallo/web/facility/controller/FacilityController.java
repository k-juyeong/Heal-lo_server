package com.kh.heallo.web.facility.controller;

import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.FacilityCriteria;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.web.ResponseMsg;
import com.kh.heallo.web.StatusCode;
import com.kh.heallo.web.facility.dto.FacilityCriteriaDto;
import com.kh.heallo.web.facility.dto.FacilityDetail;
import com.kh.heallo.web.utility.DtoModifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/facilities")
@RequiredArgsConstructor
@Slf4j
public class FacilityController {

    private final FacilitySVC facilitySVC;

    //공공데이터 연동
    @GetMapping("/api")
    public String connect(Model model) {
        Integer resultCount = facilitySVC.connect();
        model.addAttribute("resultCount", resultCount);

        return "facility/publicApiStatus";
    }

    //운동시설 검색페이지
    @GetMapping
    public String searchView() {

        return "facility/search-facility";
    }

    //운동시설 상세페이지
    @GetMapping("/{fcno}")
    public String findByFcno(@PathVariable("fcno") Long fcno, Model model) {
        Facility foundFacility = facilitySVC.findByFcno(fcno);
        FacilityDetail facilityDetail = new FacilityDetail();
        BeanUtils.copyProperties(foundFacility, facilityDetail);
        model.addAttribute("facility",facilityDetail);

        return "/facility/facility-detail";
    }
}
