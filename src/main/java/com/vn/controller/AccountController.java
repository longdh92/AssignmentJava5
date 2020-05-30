package com.vn.controller;

import com.vn.dao.AdminDAO;
import com.vn.model.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AccountController {

    @RequestMapping("/admin")
    public String index() {
        return "admin/admin";
    }

    @RequestMapping("/adminList")
    public String adminList(Model model) {
        AdminDAO adminDAO = new AdminDAO();
        List<Admin> list = adminDAO.findAllAdmin();
        model.addAttribute("adminList", list);
        model.addAttribute("user", new Admin());
        System.out.println(list.size());
        return "admin/adminList";
    }

    @RequestMapping("/loginAdmin")
    public String loginAdmin() {
        return "admin/login";
    }

    @RequestMapping("/registerAdmin")
    public String registerAdmin() {
        return "admin/register";
    }
}
