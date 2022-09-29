package com.kh.heallo.web.facility.controller;

import com.kh.heallo.domain.bookmark.Bookmark;
import com.kh.heallo.domain.bookmark.svc.BookmarkSVC;
import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.web.facility.dto.FacilityDetail;
import com.kh.heallo.web.member.session.LoginMember;
import com.kh.heallo.web.session.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/facilities")
@RequiredArgsConstructor
@Slf4j
public class FacilityController {

    private final FacilitySVC facilitySVC;
    private final BookmarkSVC bookmarkSVC;

    //공공데이터 연동
    @GetMapping("/api")
    public String connect(Model model) {
        Map<String, Integer> result = facilitySVC.connect();
        model.addAttribute("addCount", result.get("addCount"));
        model.addAttribute("updateCount", result.get("updateCount"));

        return "search-facility/publicApiStatus";
    }

    //운동시설 검색페이지
    @GetMapping
    public String searchView(
            @RequestParam(value = "keyword",required = false) String keyword,
            Model model
    ) {

        if (keyword != null) {
            model.addAttribute("keyword", keyword);
        }

        return "search-facility/search-page";
    }

    //운동시설 상세페이지
    @GetMapping("/{fcno}")
    public String findByFcno(HttpServletRequest request ,@PathVariable("fcno") Long fcno, Model model) {

        // 운동시설 상세조회
        Facility foundFacility = facilitySVC.findByFcno(fcno);

        // Facility => FacilityDetail
        FacilityDetail facilityDetail = new FacilityDetail();
        BeanUtils.copyProperties(foundFacility, facilityDetail);

        //로그인 계정 즐겨찾기 목록 조회
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
            LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
            List<Bookmark> bookmarkList = bookmarkSVC.findBookmarkListByMemno(loginMember.getMemno());

            //즐겨찾기 여부
            for (Bookmark bookmark : bookmarkList) {
                if (facilityDetail.getFcno().equals(bookmark.getFcno())) {
                    facilityDetail.setBookmarking(true);
                    break;
                }
            }
        }

        model.addAttribute("facility",facilityDetail);

        return "search-facility/facility-page";
    }
}
