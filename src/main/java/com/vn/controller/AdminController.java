package com.vn.controller;

import com.vn.dao.AdminDAO;
import com.vn.model.Admin;
import com.vn.service.AdminService;
import com.vn.service.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    AdminDAO adminDAO = new AdminDAO();

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

    @GetMapping("/loginAdmin")
    public String loginAdmin(@CookieValue(value = "usernameAdmin", defaultValue = "") String usernameAdmin, @CookieValue(value = "passwordAdmin", defaultValue = "") String passwordAdmin, Model model) {
        model.addAttribute("usernameAdmin", usernameAdmin);
        model.addAttribute("passwordAdmin", passwordAdmin);
        List<Admin> list = adminService.findAll();
        System.out.println(list.size());
        return "admin/login";
    }

    @RequestMapping("/registerAdmin")
    public String registerAdmin() {
        return "admin/register";
    }

    @PostMapping("/loginAdmin")
    public String loginAdmin1(Admin admin, Model model, HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        List<Admin> list = adminDAO.findAllAdmin();
        for (Admin admin1 : list) {
            if (admin.getUsernameAdmin().equals(admin1.getUsernameAdmin()) && admin.getPasswordAdmin().equals(admin1.getPasswordAdmin())) {
                if (request.getParameter("rememberAdmin") != null) {
                    Cookie usernameAdmin = new Cookie("usernameAdmin", admin.getUsernameAdmin());
                    usernameAdmin.setMaxAge(3600);
                    response.addCookie(usernameAdmin);
                    Cookie passwordAdmin = new Cookie("passwordAdmin", admin.getPasswordAdmin());
                    passwordAdmin.setMaxAge(3600);
                    response.addCookie(passwordAdmin);
                    model.addAttribute("usernameAdmin", usernameAdmin);
                    model.addAttribute("passwordAdmin", passwordAdmin);
                }
                return "admin/admin";
            }
        }
        return "admin/login";
    }

    public Admin checkCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Admin admin = null;
        String usernameAdmin = "", passwordAdmin = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("usernameAdmin")) {
                usernameAdmin = cookie.getValue();
            }
            if (cookie.getName().equalsIgnoreCase("passwordAdmin")) {
                passwordAdmin = cookie.getValue();
            }
        }
        if (!usernameAdmin.isEmpty() && !passwordAdmin.isEmpty()) {
            admin = new Admin();
            admin.setUsernameAdmin(usernameAdmin);
            admin.setPasswordAdmin(passwordAdmin);
        }
        return admin;
    }
}
