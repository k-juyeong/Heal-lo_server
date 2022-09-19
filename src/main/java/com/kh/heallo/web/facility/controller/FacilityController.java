package com.kh.heallo.web.facility.controller;

import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.web.facility.dto.FacilityDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public String searchView(HttpServletRequest request) {

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
