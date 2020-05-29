package com.vn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccountController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

}
