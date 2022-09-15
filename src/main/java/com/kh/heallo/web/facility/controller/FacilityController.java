package com.kh.heallo.web.facility.controller;

import com.kh.heallo.domain.facility.FacilityCriteria;
import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.web.StatusCode;
import com.kh.heallo.web.utility.DtoModifier;
import com.kh.heallo.web.ResponseMsg;
import com.kh.heallo.web.facility.dto.FacilityCriteriaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/facilities")
@RequiredArgsConstructor
@Slf4j
public class FacilityController {

    private final FacilitySVC facilitySVC;
    private final DtoModifier dtoModifier;

    //공공데이터 연동
    @GetMapping("/api")
    public String connect(Model model) {
        //Get resultCount
        Integer resultCount = facilitySVC.connect();

        //model addAttribute]
        model.addAttribute("resultCount", resultCount);
        return "facility/publicApiStatus";
    }

    //운동시설 검색페이지
    @GetMapping
    public String searchView() {

        return "facility/search-facility";
    }

    //검색 결과 수
    @ResponseBody
    @GetMapping("/total")
    public ResponseEntity<ResponseMsg> totalCount(@ModelAttribute FacilityCriteriaDto searchCriteria) {
        //SearchCriteria => FacilityCriteria
        FacilityCriteria facilityCriteria = dtoModifier.getFacilityCriteria(searchCriteria);

        //Get totalCount
        Integer totalCount = facilitySVC.getTotalCount(facilityCriteria);

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .setStatusCode(StatusCode.SUCCESS)
                .setMessage("검색에 대한 결과 수를 조회했습니다.")
                .setData("totalCount", totalCount);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    //검색 결과(페이징) **
    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity<ResponseMsg> search(@ModelAttribute FacilityCriteriaDto searchCriteria) {
        //SearchCriteria => FacilityCriteria
        FacilityCriteria criteria = dtoModifier.getFacilityCriteria(searchCriteria);

        //Get List<Facility>
        List<Facility> facilityList = facilitySVC.search(criteria);

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .setStatusCode(StatusCode.SUCCESS)
                .setMessage("검색에 대한 결과를 조회했습니다.")
                .setData("facilities", facilityList);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    //운동시설 상세페이지
    @GetMapping("/{fcno}")
    public String findByFcno(@PathVariable("fcno") Long fcno, Model model) {
        //Get Facility
        Facility foundFacility = facilitySVC.findByFcno(fcno);

        //model addAttribute
        model.addAttribute("facility",foundFacility);

        return "/facility/facility-detail";
    }
}
