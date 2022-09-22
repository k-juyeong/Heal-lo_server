package com.kh.heallo.web.facility.controller;

import com.kh.heallo.domain.bookmark.Bookmark;
import com.kh.heallo.domain.bookmark.svc.BookmarkSVC;
import com.kh.heallo.domain.facility.AutoComplete;
import com.kh.heallo.domain.facility.FacilityCriteria;
import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.domain.review.svc.ReviewSVC;
import com.kh.heallo.web.facility.dto.AutoCompleteDto;
import com.kh.heallo.web.member.session.LoginMember;
import com.kh.heallo.web.response.StatusCode;
import com.kh.heallo.web.facility.dto.FacilitySearchDto;
import com.kh.heallo.web.session.Session;
import com.kh.heallo.web.utility.DtoModifier;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.facility.dto.FacilityCriteriaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/facilities")
@RequiredArgsConstructor
@Slf4j
public class FacilityRestController {

    private final FacilitySVC facilitySVC;
    private final DtoModifier dtoModifier;
    private final BookmarkSVC bookmarkSVC;

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

    //검색 결과(페이징)
    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity<ResponseMsg> search(
            HttpServletRequest request,
            @ModelAttribute FacilityCriteriaDto searchCriteria) {

        //FacilityCriteriaDto => FacilityCriteria
        FacilityCriteria criteria = dtoModifier.getFacilityCriteria(searchCriteria);

        //검색
        List<Facility> facilityList = facilitySVC.search(criteria);

        //List<Facility> =>  List<FacilitySearchDto>
        List<FacilitySearchDto> facilitySearchDtos = facilityList.stream().map(facility -> {
            FacilitySearchDto facilitySearchDto = new FacilitySearchDto();
            BeanUtils.copyProperties(facility, facilitySearchDto);

            return facilitySearchDto;
        }).collect(Collectors.toList());

        //로그인 계정 즐겨찾기 목록 조회
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
            LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
            List<Bookmark> bookmarkList = bookmarkSVC.findBookmarkListByMemno(loginMember.getMemno());

            //즐겨찾기 여부
            facilitySearchDtos = facilitySearchDtos.stream().map(facilitySearchDto -> {
                Optional<Bookmark> optionalBookmark = bookmarkList
                        .stream()
                        .filter(bookmark -> facilitySearchDto.getFcno().equals(bookmark.getFcno()))
                        .findFirst();
                if (optionalBookmark.isPresent()) {
                    facilitySearchDto.setBookmarking(true);
                }

                return facilitySearchDto;
            }).collect(Collectors.toList());
        }

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("facilities", facilitySearchDtos);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    //상호명 자동완성
    @ResponseBody
    @GetMapping("/auto-complete")
    public ResponseEntity<ResponseMsg> autoComplete(
            FacilityCriteriaDto criteriaDto
    ) {
        FacilityCriteria facilityCriteria = dtoModifier.getFacilityCriteria(criteriaDto);
        List<AutoComplete> autoCompleteData = facilitySVC.autoComplete(facilityCriteria, facilityCriteria.getNumOfRow());
        List<AutoCompleteDto> autoCompleteDtos = dtoModifier.getAutoCompleteDto(autoCompleteData);

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("autoComplete", autoCompleteDtos);

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
