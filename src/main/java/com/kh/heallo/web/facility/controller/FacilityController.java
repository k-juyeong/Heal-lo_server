package com.kh.heallo.web.facility.controller;

import com.kh.heallo.domain.facility.FacilityCriteria;
import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.web.DtoModifier;
import com.kh.heallo.web.ResponseMsg;
import com.kh.heallo.web.facility.dto.SearchCriteria;
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

    //검색 전체 갯수
    @ResponseBody
    @GetMapping("/total")
    public ResponseEntity<ResponseMsg> totalCount(@ModelAttribute SearchCriteria searchCriteria) {
        //SearchCriteria => FacilityCriteria
        FacilityCriteria facilityCriteria = dtoModifier.getFacilityCriteria(searchCriteria);

        //Get totalCount
        Integer totalCount = facilitySVC.getTotalCount(facilityCriteria);

        //Create ResponseEntity
        Map<String, Object> data = new HashMap<>();
        data.put("totalCount", totalCount);
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseMsg responseMsg = new ResponseMsg("OK","성공", data);

        return new ResponseEntity<>(responseMsg,headers,HttpStatus.OK);
    }

    //검색 결과(페이징)
    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity<ResponseMsg> search(@ModelAttribute SearchCriteria searchCriteria) {
        //SearchCriteria => FacilityCriteria
        FacilityCriteria criteria = dtoModifier.getFacilityCriteria(searchCriteria);

        //Get List<Facility>
        List<Facility> facilityList = facilitySVC.search(criteria);

        //Create ResponseEntity
        Map<String, Object> data = new HashMap<>();
        data.put("facilities", facilityList);
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseMsg responseMsg = new ResponseMsg("OK","성공", data);

        return new ResponseEntity<>(responseMsg,headers,HttpStatus.OK);
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
