package com.kh.heallo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class HomeController {

    public String home() {

        return "/";
    }
}
