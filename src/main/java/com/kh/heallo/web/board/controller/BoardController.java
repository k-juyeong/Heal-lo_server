package com.kh.heallo.web.board.controller;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.board.dao.BbsFilterCondition;
import com.kh.heallo.domain.board.svc.BoardSVC;
import com.kh.heallo.domain.common.paging.FindCriteria;
import com.kh.heallo.web.board.dto.DetailForm;
import com.kh.heallo.web.board.dto.EditForm;
import com.kh.heallo.web.board.dto.SaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
  private final BoardSVC boardSVC;



  @Autowired
  @Qualifier("fc10_5") //동일한 타입의 객체가 여러개있을때 빈이름을 명시적으로 지정해서 주입받을때
  private FindCriteria fc;

  //등록양식
  @GetMapping("/add")
  public String saveForm( Long memno, Model model,
                         HttpServletRequest request) {

//     //로그인 여부
//    HttpSession session2 = request.getSession(false);
//    if (session2 != null) {
//      session2.invalidate();
//    }

//    //회원번호 조회
//    HttpSession session = request.getSession(false);
//    if(session != null && session.getAttribute(Session.LOGIN_MEMBER.name()) != null) {
//      LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
//      memno = loginMember.getMemno();
//    }
//    LoginMember loginMember = (LoginMember) session.getAttribute(Session.LOGIN_MEMBER.name());
//    SaveForm saveForm = new SaveForm();
//    saveForm.setMemno(memno);
//    saveForm.setMemnickname(loginMember.getMemnickname());


    model.addAttribute("form", new SaveForm());

    return "board/saveForm";
  }


  //등록
  @PostMapping("/add")
  public String add(@Valid @ModelAttribute("form") SaveForm saveForm,
                    BindingResult bindingResult,
                    RedirectAttributes redirectAttributes,
                    HttpServletRequest request){
    //기본검증
    if(bindingResult.hasErrors()){
      log.info("bindingResult={}", bindingResult);
      return "board/saveForm";
    }

    //필드검증
    //제목 30글자 이내.
    if(saveForm.getBdtitle().length() > 100){
      bindingResult.rejectValue("bdtitle","board.bdtitle",new Integer[]{30},"제목 글자수 초과");
      log.info("bindingResult={}", bindingResult);
      return "board/saveForm";
    }

    Board board = new Board();
    BeanUtils.copyProperties(saveForm, board);
    Long bdno = boardSVC.save(board);

    redirectAttributes.addAttribute("id", bdno);
    return "redirect:/boards/{id}/detail";
  }




  //조회
  @GetMapping("/{id}/detail")
  public String findById(@PathVariable("id") Long boardId,
                         Model model){
    Optional<Board> findedBoard = boardSVC.findByBoardId(boardId);
    DetailForm detailForm = new DetailForm();
    if(!findedBoard.isEmpty()) {
      BeanUtils.copyProperties(findedBoard.get(), detailForm);
    }
    model.addAttribute("form", detailForm);
    return "board/detailForm";
  }



  //수정양식
  @GetMapping("/{id}/edit")
  public String updateForm(@PathVariable("id") Long boardId,
                           Model model) {

    Optional<Board> findedBoard = boardSVC.findByBoardId(boardId);
    EditForm updateForm = new EditForm();
    if(!findedBoard.isEmpty()) {
      BeanUtils.copyProperties(findedBoard.get(), updateForm);
    }
    model.addAttribute("form", updateForm);

    return "board/updateForm";
  }





  //수정
  @PostMapping("/{id}/edit")
  public String update(@PathVariable("id") Long boardId,
                       @Valid @ModelAttribute("form") EditForm editForm,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes){

    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "board/updateForm";
    }
    //필드검증
    //제목 30글자 이내.
    if(editForm.getBdtitle().length() > 30){
      bindingResult.rejectValue("bdtitle","board.bdtitle",new Integer[]{30},"제목 글자수 초과");
      log.info("bindingResult={}", bindingResult);
      return "board/saveForm";
    }

    Board board = new Board();
    BeanUtils.copyProperties(editForm, board);
    boardSVC.update(boardId, board);

    redirectAttributes.addAttribute("id", boardId);
    return "redirect:/boards/{id}/detail";

  }




  //삭제
  @GetMapping("/{id}/del")
  public String del(@PathVariable("id") Long boardId) {

    boardSVC.deleteByBoardId(boardId);

    return "redirect:/boards";  //항시 절대경로로
  }




  //목록
  @GetMapping({"",
      "/{reqPage}",
      "/{reqPage}//",
      "/{reqPage}/{searchType}/{keyword}"})
  public String findAll(
      @PathVariable(required = false) Optional<Integer> reqPage,
      @PathVariable(required = false) Optional<String> searchType,
      @PathVariable(required = false) Optional<String> keyword,
      @RequestParam(required = false) Optional<String> bdcg,
      Model model) {

    log.info("/list 요청됨{},{},{},{}",reqPage,searchType,keyword,bdcg);

    String cate = getCategory(bdcg);


    //FindCriteria 값 설정
    fc.getRc().setReqPage(reqPage.orElse(1)); //요청페이지, 요청없으면 1
    fc.setSearchType(searchType.orElse(""));  //검색유형
    fc.setKeyword(keyword.orElse(""));        //검색어

    List<Board> list = null;
    //게시물 목록 전체
    if(bdcg == null || StringUtils.isEmpty(cate)) {
      log.info("1");
      //검색어 있음
      if(searchType.isPresent() && keyword.isPresent()){
        BbsFilterCondition filterCondition = new BbsFilterCondition(
            "",fc.getRc().getStartRec(), fc.getRc().getEndRec(),
            searchType.get(),
            keyword.get());
        fc.setTotalRec(boardSVC.totalCount(filterCondition));
        fc.setSearchType(searchType.get());
        fc.setKeyword(keyword.get());
        list = boardSVC.findAll(filterCondition);
        log.info("2");
        //검색어 없음
      }else {
        log.info("3");
        //총레코드수
        fc.setTotalRec(boardSVC.totalCount());
        list = boardSVC.findAll(fc.getRc().getStartRec(), fc.getRc().getEndRec());
      }

      //카테고리별 목록
    }else{
      log.info("4");
      //검색어 있음
      if(searchType.isPresent() && keyword.isPresent()){
        BbsFilterCondition filterCondition = new BbsFilterCondition(
            bdcg.get(),fc.getRc().getStartRec(), fc.getRc().getEndRec(),
            searchType.get(),
            keyword.get());
        fc.setTotalRec(boardSVC.totalCount(filterCondition));
        fc.setSearchType(searchType.get());
        fc.setKeyword(keyword.get());
        list = boardSVC.findAll(filterCondition);
        log.info("5");
        //검색어 없음
      }else {
        log.info("6");
        fc.setTotalRec(boardSVC.totalCount(cate));
        log.info("{}-{}-{}-{}-{}",cate, fc.getRc().getStartRec(), fc.getRc().getEndRec());
        list = boardSVC.findAll(cate, fc.getRc().getStartRec(), fc.getRc().getEndRec());
        log.info("size={}", list.size());
      }
    }



    List<Board> list2 = new ArrayList<>();
    list.stream().forEach(board->{
      log.info("red={}",board);
      BeanUtils.copyProperties(board, new DetailForm());
      list2.add(board);
    });
    model.addAttribute("list", list2);
    model.addAttribute("fc",fc);
    model.addAttribute("category", cate);

    return "board/all";
  }



  //쿼리스트링 카테고리 읽기, 없으면 ""반환
  private String getCategory(Optional<String> category) {
    String cate = category.isPresent()? category.get():"";
    log.info("category={}", cate);
    return cate;
  }

}
