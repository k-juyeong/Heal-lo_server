package com.kh.heallo.web.review.controller;

import com.kh.heallo.domain.common.paging.PageCriteria;
import com.kh.heallo.domain.facility.svc.FacilitySVC;
import com.kh.heallo.domain.review.OrderBy;
import com.kh.heallo.domain.review.Review;
import com.kh.heallo.domain.review.ReviewCriteria;
import com.kh.heallo.domain.review.svc.ReviewSVC;
import com.kh.heallo.domain.uploadfile.FileData;
import com.kh.heallo.domain.uploadfile.svc.UploadFileSVC;
import com.kh.heallo.web.session.LoginMember;
import com.kh.heallo.web.response.ResponseMsg;
import com.kh.heallo.web.response.StatusCode;
import com.kh.heallo.web.review.ReviewFileValidator;
import com.kh.heallo.web.session.Session;
import com.kh.heallo.web.utility.DtoModifier;
import com.kh.heallo.web.review.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewRestController {

    private final ReviewSVC reviewSVC;
    private final FacilitySVC facilitySVC;
    private final UploadFileSVC uploadFileSVC;
    private final DtoModifier dtoModifier;
    private final ReviewFileValidator fileValidator;

    @Autowired
    @Qualifier("pc10_5")
    private PageCriteria pc;

    //해당 운동시설 리뷰 리스트 조회(페이징)
    @GetMapping("/{fcno}/list")
    public ResponseEntity<ResponseMsg> findListByFcno(
            HttpServletRequest request,
            @PathVariable("fcno") Long fcno,
            @ModelAttribute ReviewCriteriaDto reviewCriteriaDto
    ) {

        //정렬기준 셋팅
        switch(reviewCriteriaDto.getOrderBy()) {
            case "dateAsc": reviewCriteriaDto.setOrderBy(OrderBy.RV_DATE_ASC.getOrderBy());
            break;
            case "dateDesc": reviewCriteriaDto.setOrderBy(OrderBy.RV_DATE_DESC.getOrderBy());
            break;
            case "scoreDsc": reviewCriteriaDto.setOrderBy(OrderBy.RV_SCORE_DSC.getOrderBy());
            break;
            default: reviewCriteriaDto.setOrderBy(OrderBy.RV_DATE_ASC.getOrderBy());
        }

        //회원번호 조회
        HttpSession session = request.getSession(false);
        Long memno = null;
        if(session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
            LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
            memno = loginMember.getMemno();
        }

        //FindCriteria 설정
        pc.getRc().setReqPage(reviewCriteriaDto.getPageNo());

        //ReviewCriteriaDto => ReviewCriteria
        ReviewCriteria reviewCriteria = dtoModifier.getReviewCriteria(reviewCriteriaDto);
        reviewCriteria.setStartNo(pc.getRc().getStartRec());
        reviewCriteria.setEndNo(pc.getRc().getEndRec());

        //토탈 카운트 검색
        Integer totalCount = reviewSVC.getTotalCount(fcno);
        pc.setTotalRec(totalCount);

        //리뷰 조회
        Long finalMemno = memno;
        List<ReviewDto> reviewDtos = reviewSVC
                .findListByFcno(fcno, reviewCriteria)
                .stream()
                .map(review -> {
                    ReviewDto reviewDto = dtoModifier.getReviewDto(review, finalMemno);
                    return reviewDto;
                })
                .collect(Collectors.toList());

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("pagination", pc)
                .setData("reviews", reviewDtos);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    //최근 등록된 이미지 상위 5개
    @GetMapping("/{fcno}/new-images")
    private ResponseEntity<ResponseMsg> findNewReviewImage(@PathVariable("fcno") Long fcno) {

        //이미지 조회
        List<FileData> newReviewImage = uploadFileSVC.findNewReviewImage(fcno);

        //이미지가 있다면
        List<ReviewFileData> reviewFileDataList = dtoModifier.getReviewFileDataList(newReviewImage);

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("images", reviewFileDataList);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    //리뷰 등록 처리
    @PostMapping("/{fcno}")
    public ResponseEntity<ResponseMsg> add(
            HttpServletRequest request,
            @PathVariable("fcno") Long fcno,
            @Valid @ModelAttribute AddReviewForm addReviewForm,
            @RequestParam(required = false) List<MultipartFile> multipartFiles
    ) throws BindException {

        //세션이 없을 시 메세지 반환
        ResponseEntity notLoginResponseMsg = (ResponseEntity) request.getAttribute(Session.NOT_LOGIN.name());
        if (request.getAttribute(Session.NOT_LOGIN.name()) != null) {

            return notLoginResponseMsg;
        }

        //회원번호 조회
        HttpSession session = request.getSession(false);
        LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
        Long memno = loginMember.getMemno();

        //파일 검증
        if (multipartFiles != null) {
            fileValidator.validate(
                    multipartFiles,
                    new BindException(addReviewForm, "addReviewForm"),
                    multipartFiles.size()
            );
        }

        //리뷰등록
        Review review = dtoModifier.getReviewByAddReviewForm(addReviewForm);
        review.setFcno(fcno);
        review.setMemno(memno);
        if (multipartFiles != null) {
            reviewSVC.add(review, multipartFiles);
        } else {
            reviewSVC.add(review);
        }

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("redirect", "/facilities/" + fcno);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    //리뷰 수정 처리
    @PatchMapping("/{rvno}")
    public ResponseEntity<ResponseMsg> update(
            HttpServletRequest request,
            @PathVariable("rvno") Long rvno,
            @Valid @ModelAttribute EditReviewForm editReviewForm,
            @RequestParam(required = false) List<MultipartFile> multipartFiles
    ) throws BindException {

        //세션이 없을 시 메세지 반환
        ResponseEntity notLoginResponseMsg = (ResponseEntity) request.getAttribute(Session.NOT_LOGIN.name());
        if (request.getAttribute(Session.NOT_LOGIN.name()) != null) {

            return notLoginResponseMsg;
        }

        //회원번호 조회
        HttpSession session = request.getSession(false);
        LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
        Long memno = loginMember.getMemno();

        int imgSize = reviewSVC.findByRvno(rvno).getImageFiles().size();

        //파일검증
        if (multipartFiles != null) {
            int totalSize = imgSize
                            - editReviewForm.getDeleteImages().length
                            + multipartFiles.size();
            fileValidator.validate(
                    multipartFiles,
                    new BindException(editReviewForm, "editReviewForm"),
                    imgSize
            );
        }

        //리뷰등록
        Review review = dtoModifier.getReviewByEditReviewForm(editReviewForm);
        review.setFcno(reviewSVC.findByRvno(rvno).getFcno());
        Long[] deleteImages = editReviewForm.getDeleteImages();

        if (multipartFiles != null && deleteImages != null) {
            reviewSVC.update(review, multipartFiles, deleteImages);

        } else if (multipartFiles != null){
            reviewSVC.update(review, multipartFiles);

        } else if (deleteImages != null) {
            reviewSVC.update(review,deleteImages);

        } else {
            reviewSVC.update(review);

        }

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("redirect", "/facilities/" + reviewSVC.findByRvno(rvno).getFcno());

        return new ResponseEntity(responseMsg, HttpStatus.OK);
    }

    //리뷰 삭제 처리
    @DeleteMapping("/{rvno}")
    public ResponseEntity<ResponseMsg> delete(HttpServletRequest request, @PathVariable Long rvno) {

        //세션이 없을 시 메세지 반환
        ResponseEntity notLoginResponseMsg = (ResponseEntity) request.getAttribute(Session.NOT_LOGIN.name());
        if (request.getAttribute(Session.NOT_LOGIN.name()) != null) {

            return notLoginResponseMsg;
        }

        //리뷰 삭제
        Long fcno = reviewSVC.findByRvno(rvno).getFcno();
        Integer resultCount = reviewSVC.delete(rvno,fcno);

        //Create ResponseEntity
        ResponseMsg responseMsg = new ResponseMsg()
                .createHeader(StatusCode.SUCCESS)
                .setData("resultCount",resultCount);

        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
}
