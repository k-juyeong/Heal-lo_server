package com.kh.heallo.web.reply.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/reply")
public class ReplyController {


  @GetMapping
  public String reply() {

    return "reply/reply";
  }
}
