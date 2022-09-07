package com.kh.heallo.web.facility.controller;

import com.kh.heallo.domain.facility.Criteria;
import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.web.ResponseMsg;
import com.kh.heallo.web.facility.dto.SearchCriteria;
import com.kh.heallo.web.facility.dto.TotalCountCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/api")
    public String connect(Model model) {
        int resultCount = facilitySVC.connect();

        model.addAttribute("resultCount", resultCount);
        return "facility/publicApiStatus";
    }

    @GetMapping
    public String searchView() {

        return "facility/search-facility";
    }

    @ResponseBody
    @GetMapping("/total")
    public ResponseEntity<ResponseMsg> totalCount(@ModelAttribute TotalCountCriteria totalCountCriteria) {
        Criteria criteria = new Criteria();
        criteria.setFctype(totalCountCriteria.getFctype());
        criteria.setFcaddr(totalCountCriteria.getFcaddr());
        criteria.setFcname(totalCountCriteria.getFcname());
        int totalCount = facilitySVC.getTotalCount(criteria);

        Map<String, Object> data = new HashMap<>();
        data.put("totalCount", totalCount);
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseMsg responseMsg = new ResponseMsg("OK","성공", data);

        return new ResponseEntity<>(responseMsg,headers,HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity<ResponseMsg> search(@ModelAttribute SearchCriteria searchCriteria) {
        Criteria criteria = new Criteria();
        criteria.setFctype(searchCriteria.getFctype());
        criteria.setFcaddr(searchCriteria.getFcaddr());
        criteria.setFcname(searchCriteria.getFcname());
        criteria.setPageNo(searchCriteria.getPageNo());
        criteria.setNumOfRow(searchCriteria.getNumOfRow());
        List<Facility> facilityList = facilitySVC.search(criteria);

        Map<String, Object> data = new HashMap<>();
        data.put("items", facilityList);
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseMsg responseMsg = new ResponseMsg("OK","성공", data);

        return new ResponseEntity<>(responseMsg,headers,HttpStatus.OK);
    }

}
