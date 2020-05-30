package com.vn.controller;

import com.vn.dao.AdminDAO;
import com.vn.model.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Properties;

@Controller
public class AccountController {

    @RequestMapping("/trang-chu")
    public String index() {
        AdminDAO adminDAO = new AdminDAO();
        List<Admin> list = adminDAO.findAllAdmin();
        System.out.println(list.size());
        return "admin/admin";
    }

    @RequestMapping("/adminList")
    public String adminList() {
        return "admin/adminList";
    }
}
