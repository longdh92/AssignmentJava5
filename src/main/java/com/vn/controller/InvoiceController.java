package com.vn.controller;

import com.vn.model.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class InvoiceController {
    @RequestMapping("/invoiceListView")
    public String invoiceList(HttpSession session, Model model) {
        String checkLogin = checkLogin(session, model);
        if (checkLogin.length() > 0) {
            return checkLogin;
        }
        return "admin/invoiceList";
    }

    public String checkLogin(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "admin/login";
        } else if (!admin.isRole() || !admin.getStatus().equalsIgnoreCase("ACTIVED")) {
            model.addAttribute("admin", admin);
            return "admin/admin";
        }
        model.addAttribute("admin", admin);
        return "";
    }
}
