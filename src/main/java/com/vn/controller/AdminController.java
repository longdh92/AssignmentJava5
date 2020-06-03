package com.vn.controller;

import com.vn.model.Admin;
import com.vn.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @RequestMapping("/admin")
    public String index() {
        return "admin/admin";
    }

    @RequestMapping("/adminList")
    public String adminList(Model model) {
        model.addAttribute("adminList", adminService.findAll());
        return "admin/adminList";
    }

    @GetMapping("/loginAdminView")
    public String loginAdmin(@ModelAttribute(value = "admin") Admin admin, @CookieValue(value = "usernameAdmin", defaultValue = "") String usernameAdmin, @CookieValue(value = "passwordAdmin", defaultValue = "") String passwordAdmin, Model model) {
        model.addAttribute("usernameAdmin", usernameAdmin);
        model.addAttribute("passwordAdmin", passwordAdmin);
        return "admin/login";
    }

    @PostMapping("/loginAdmin")
    public String loginAdmin(@ModelAttribute("admin") Admin admin, Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) {
        List<Admin> list = adminService.findAll();
        Cookie usernameAdmin = new Cookie("usernameAdmin", admin.getUsernameAdmin());
        Cookie passwordAdmin = new Cookie("passwordAdmin", admin.getPasswordAdmin());
        for (Admin admin1 : list) {
            if (admin.getUsernameAdmin().equals(admin1.getUsernameAdmin())) {
                if (admin.getPasswordAdmin().equals(admin1.getPasswordAdmin())) {
                    if (request.getParameter("rememberAdmin") != null) {
                        usernameAdmin.setMaxAge(3600);
                        response.addCookie(usernameAdmin);
                        passwordAdmin.setMaxAge(3600);
                        response.addCookie(passwordAdmin);
                        model.addAttribute("usernameAdmin", usernameAdmin);
                        model.addAttribute("passwordAdmin", passwordAdmin);
                        return "admin/admin";
                    } else {
                        usernameAdmin.setMaxAge(0);
                        response.addCookie(usernameAdmin);
                        passwordAdmin.setMaxAge(0);
                        response.addCookie(passwordAdmin);
                        model.addAttribute("usernameAdmin", usernameAdmin);
                        model.addAttribute("passwordAdmin", passwordAdmin);
                        return "admin/admin";
                    }
                } else {
                    model.addAttribute("message", "Invalid password !");
                    model.addAttribute("alert", "alert alert-danger");
                    return "admin/login";
                }
            } else {
                continue;
            }
        }
        model.addAttribute("message", "Username is not exists !");
        model.addAttribute("alert", "alert alert-danger");
        return "admin/login";
    }

    @RequestMapping("/logoutAdmin")
    public String logoutAdmin(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("usernameAdmin")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                if (cookie.getName().equals("passwordAdmin")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
        return "admin/login";
    }

    @GetMapping("/registerAdminView")
    public String registerAdmin() {
        return "admin/register";
    }

    @PostMapping("/registerAdmin")
    public String registerAdmin(Model model, HttpServletRequest request, Admin admin) {
        String message = validateRegisterAdmin(admin, model, request.getParameter("confirmPassword"));
        if (message.length() > 0) {
            return message;
        }
        adminService.save(admin);
        model.addAttribute("message", "Register Successful !");
        model.addAttribute("alert", "alert alert-success");
        return "admin/register";
    }

    public String validateRegisterAdmin(Admin admin, Model model, String confirm) {
        // Username
        if (!admin.getUsernameAdmin().matches("[a-zA-Z0-9]+")) {
            model.addAttribute("message", "Only alphabet characters and numbers !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/register";
        } else if (admin.getUsernameAdmin().trim().length() < 6) {
            model.addAttribute("message", "Username at least 6 letters !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/register";
        }
        List<Admin> list = adminService.findAll();
        for (Admin admin1 : list) {
            if (admin.getUsernameAdmin().equalsIgnoreCase(admin1.getUsernameAdmin())) {
                model.addAttribute("message", "Can not use this username");
                model.addAttribute("alert", "alert alert-danger");
                return "admin/register";
            }
        }
        // Email
        if (!admin.getEmailAdmin().matches("\\w+@\\w+(\\.\\w+){1,2}")) {
            model.addAttribute("message", "Invalid email address !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/register";
        }
        // Password
        if (admin.getPasswordAdmin().trim().length() < 3) {
            model.addAttribute("message", "Password at least 3 letters !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/register";
        } else if (!admin.getPasswordAdmin().equals(confirm)) {
            model.addAttribute("message", "Password and confirm are not match !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/register";
        }

        return "";
    }

    public String validateUpdateAdmin(Admin admin, Model model, String confirm) {
        // Email
        if (!admin.getEmailAdmin().matches("\\w+@\\w+(\\.\\w+){1,2}")) {
            model.addAttribute("message", "Invalid email address !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/editAdmin";
        }
        // Password
        if (admin.getPasswordAdmin().trim().length() < 3) {
            model.addAttribute("message", "Password at least 3 letters !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/editAdmin";
        } else if (!admin.getPasswordAdmin().equals(confirm)) {
            model.addAttribute("message", "Password and confirm are not match !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/editAdmin";
        }

        return "";
    }

    @GetMapping("/editAdminView/{idAdmin}")
    public String editAdmin(@PathVariable(value = "idAdmin") Long idAdmin, Model model) {
        Long id = (idAdmin * 2 - 74) / 4;
        Admin admin = adminService.findById(id);
        model.addAttribute("admin", admin);
        return "admin/editAdmin";
    }

    @GetMapping("/removeAdmin/{idAdmin}")
    public String removeAdmin(@PathVariable(value = "idAdmin") Long idAdmin, Model model) {
        Long id = (idAdmin * 2 - 74) / 4;
        adminService.remove(id);
        model.addAttribute("message", "Delete Admin Successful !");
        model.addAttribute("alert", "alert alert-success");
        model.addAttribute("adminList", adminService.findAll());
        return "admin/adminList";
    }

    @PostMapping("/editAdmin")
    public String editAdmin(Admin admin, Model model, HttpServletRequest request) {
        String message = validateUpdateAdmin(admin, model, request.getParameter("confirm"));
        if (message.length() > 0) {
            return message;
        }
        adminService.save(admin);
        model.addAttribute("message", "Register Successful !");
        model.addAttribute("alert", "alert alert-success");
        admin = new Admin();
        model.addAttribute("admin", admin);
        return "admin/editAdmin";
    }

    public Admin checkCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Admin admin = new Admin();
        String usernameAdmin = "", passwordAdmin = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("usernameAdmin")) {
                usernameAdmin = cookie.getValue();
            }
            if (cookie.getName().equalsIgnoreCase("passwordAdmin")) {
                passwordAdmin = cookie.getValue();
            }
        }
        admin.setUsernameAdmin(usernameAdmin);
        admin.setPasswordAdmin(passwordAdmin);
        return admin;
    }
}
