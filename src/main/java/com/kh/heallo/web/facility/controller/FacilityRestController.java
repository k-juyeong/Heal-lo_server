package com.kh.heallo.web.facility.controller;

import com.kh.heallo.domain.bookmark.Bookmark;
import com.kh.heallo.domain.bookmark.svc.BookmarkSVC;
import com.kh.heallo.domain.common.paging.PageCriteria;
import com.kh.heallo.domain.facility.AutoComplete;
import com.kh.heallo.domain.facility.FacilityCriteria;
import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    @Qualifier("pc10_5") //동일한 타입의 객체가 여러개있을때 빈이름을 명시적으로 지정해서 주입받을때
    private PageCriteria pc;

    //검색 결과(페이징)
    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity<ResponseMsg> search(
            HttpServletRequest request,
            @ModelAttribute FacilityCriteriaDto searchCriteria) {

        //FindCriteria 설정
        pc.getRc().setReqPage(searchCriteria.getPageNo());

        //FacilityCriteriaDto => FacilityCriteria
        FacilityCriteria criteria = dtoModifier.getFacilityCriteria(searchCriteria);
        criteria.setStartNo(pc.getRc().getStartRec());
        criteria.setEndNo(pc.getRc().getEndRec());

        //토탈 카운트 검색
        Integer totalCount = facilitySVC.getTotalCount(criteria);
        pc.setTotalRec(totalCount);

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
                .setData("pagination", pc)
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
        List<AutoComplete> autoCompleteData = facilitySVC.autoComplete(facilityCriteria, 10);
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

        log.info("fcscore {}", fcScore);

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("fcScore", fcScore);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
}
