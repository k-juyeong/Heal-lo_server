package com.kh.heallo.web.bookmark.controller;

import com.kh.heallo.domain.bookmark.svc.BookmarkSVC;
import com.kh.heallo.domain.facility.Facility;
import com.kh.heallo.domain.review.OrderBy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

  private final BookmarkSVC bookmarkSVC;

  //북마크 페이지
  @GetMapping
  public String bookmarkHome(@RequestParam(value = "orderBy",required = false) String order, Model model){

    if (order == null) {
      order = OrderBy.FC_NAME_ASC.getOrderBy();
    } else {
      if(order.equals("score")) order = OrderBy.FC_SCORE_DESC.getOrderBy();
      if(order.equals("date")) order = OrderBy.FC_NAME_ASC.getOrderBy();
    }

    List<Facility> bookmarks = bookmarkSVC.bookmarkPageList(order);
    List<Facility> list = new ArrayList<>();

    log.info("bookmarks={}",bookmarks);

    bookmarks.stream().forEach(bookmark -> {
      Facility bookmark1 = new Facility();
      BeanUtils.copyProperties(bookmark,bookmark1);
      list.add(bookmark1);
    });

    log.info("list={}",list);

    model.addAttribute("list",list);

    return "bookmark/bookmark";
  }
}
