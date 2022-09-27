package com.kh.heallo.web.bookmark;

import com.kh.heallo.domain.bookmark.svc.BookmarkSVC;
import com.kh.heallo.domain.facility.Facility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkPageController {

  private final BookmarkSVC bookmarkSVC;

  //북마크 페이지
  @GetMapping
  public String bookmarkHome(Model model){

    List<Facility> bookmarks = bookmarkSVC.bookmarkPageList();
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
