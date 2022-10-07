package com.kh.heallo.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    @GetMapping
    public String home() {

        return "index";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
