package com.kh.heallo.web.board.controller;

import com.kh.heallo.domain.board.Board;
import com.kh.heallo.domain.board.svc.BoardSVC;
import com.kh.heallo.web.board.dto.DetailForm;
import com.kh.heallo.web.board.dto.EditForm;
import com.kh.heallo.web.board.dto.SaveForm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
//@RequiredArgsConstructor
@AllArgsConstructor
@RequestMapping("/boards")
public class BoardController {
  private final BoardSVC boardSVC;


  //등록양식
  @GetMapping("/add")
  public String saveForm(Model model) {
    model.addAttribute("form", new SaveForm());
    return "board/saveForm";
  }


  //등록
  @PostMapping("/add")
  public String add(@Valid @ModelAttribute("form") SaveForm saveForm,
                    BindingResult bindingResult,
                    RedirectAttributes redirectAttributes){
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
  @GetMapping
  public String findAll(Model model) {
    List<Board> boards = boardSVC.findAll();
    List<Board> list = new ArrayList<>();

    boards.stream().forEach(board->{
      BeanUtils.copyProperties(board, new DetailForm());
      list.add(board);
    });
    model.addAttribute("list", list);
    return "board/all";
  }
}
