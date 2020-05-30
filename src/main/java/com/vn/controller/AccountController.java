package com.vn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Properties;

@Controller
public class AccountController {

    @RequestMapping("/trang-chu")
    public String index() {
        return "admin/admin";
    }

    @RequestMapping("/adminList")
    public String adminList() {
        return "admin/adminList";
    }
}
