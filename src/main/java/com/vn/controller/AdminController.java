package com.vn.controller;

import com.vn.model.Admin;
import com.vn.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/admin")
    public String index(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "admin/login";
        }
        model.addAttribute("admin", admin);
        return "admin/admin";
    }

    @RequestMapping("/adminList")
    public String adminList(HttpSession session, Model model) {
        String checkLogin = checkLogin(session, model);
        if (checkLogin.length() > 0) {
            return checkLogin;
        }
        model.addAttribute("adminList", adminService.findAll());
        return "admin/adminList";
    }

    @GetMapping("/loginAdminView")
    public String loginAdmin(@CookieValue(value = "usernameAdmin", defaultValue = "") String usernameAdmin,
                             @CookieValue(value = "passwordAdmin", defaultValue = "") String passwordAdmin,
                             Model model) {
        model.addAttribute("usernameAdmin", usernameAdmin);
        model.addAttribute("passwordAdmin", passwordAdmin);
        return "admin/login";
    }

    @PostMapping("/loginAdmin")
    public String loginAdmin(Admin admin, Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) {
        List<Admin> list = adminService.findAll();
        for (Admin admin1 : list) {
            if (admin.getUsernameAdmin().equals(admin1.getUsernameAdmin())) {
                if (bCryptPasswordEncoder.matches(admin.getPasswordAdmin(), admin1.getPasswordAdmin())) {
                    Cookie usernameAdmin = new Cookie("usernameAdmin", admin.getUsernameAdmin());
                    Cookie passwordAdmin = new Cookie("passwordAdmin", admin.getPasswordAdmin());
                    if (request.getParameter("rememberAdmin") != null) {
                        usernameAdmin.setMaxAge(3600);
                        passwordAdmin.setMaxAge(3600);
                    } else {
                        usernameAdmin.setMaxAge(0);
                        passwordAdmin.setMaxAge(0);
                    }
                    response.addCookie(usernameAdmin);
                    response.addCookie(passwordAdmin);
                    session.setAttribute("admin", admin1);
                    model.addAttribute("admin", admin1);
                    return "admin/admin";
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
    public String logoutAdmin(HttpServletRequest request, HttpSession session, Model model) {
        Admin admin = checkCookie(request);
        model.addAttribute("usernameAdmin", admin.getUsernameAdmin());
        model.addAttribute("passwordAdmin", admin.getPasswordAdmin());
        session.removeAttribute("admin");
        return "admin/login";
    }

    @GetMapping("/registerAdminView")
    public String registerAdmin() {
        return "admin/register";
    }

    @PostMapping("/registerAdmin")
    public String registerAdmin(Model model, HttpServletRequest request, Admin admin) {
        String validateRegisterAdmin = validateRegisterAdmin(admin, model, "admin/register");
        if (validateRegisterAdmin.length() > 0) {
            return validateRegisterAdmin;
        }
        String validateUpdateAdmin = validateUpdateAdmin(admin, model, request.getParameter("confirmPassword"), "admin/register");
        if (validateUpdateAdmin.length() > 0) {
            return validateUpdateAdmin;
        }
        admin.setRole(false);
        admin.setStatus("INACTIVE");
        admin.setPasswordAdmin(bCryptPasswordEncoder.encode(admin.getPasswordAdmin()));
        adminService.save(admin);
        model.addAttribute("message", "Register Successful !");
        model.addAttribute("alert", "alert alert-success");
        return "admin/register";
    }

    public String validateRegisterAdmin(Admin admin, Model model, String view) {
        // Username
        if (!admin.getUsernameAdmin().matches("[a-zA-Z0-9]+")) {
            model.addAttribute("message", "Only alphabet characters and numbers !");
            model.addAttribute("alert", "alert alert-danger");
            return view;
        } else if (admin.getUsernameAdmin().trim().length() < 6) {
            model.addAttribute("message", "Username at least 6 letters !");
            model.addAttribute("alert", "alert alert-danger");
            return view;
        }
        List<Admin> list = adminService.findAll();
        for (Admin admin1 : list) {
            if (admin.getUsernameAdmin().equalsIgnoreCase(admin1.getUsernameAdmin())) {
                model.addAttribute("message", "Can not use this username");
                model.addAttribute("alert", "alert alert-danger");
                return view;
            }
        }
        // Email
        if (!admin.getEmailAdmin().matches("\\w+@\\w+(\\.\\w+){1,2}")) {
            model.addAttribute("message", "Invalid email address !");
            model.addAttribute("alert", "alert alert-danger");
            return view;
        }

        return "";
    }

    public String validateUpdateAdmin(Admin admin, Model model, String confirm, String view) {
        // Password
        if (admin.getPasswordAdmin().trim().length() < 3) {
            model.addAttribute("message", "Password at least 3 letters !");
            model.addAttribute("alert", "alert alert-danger");
            return view;
        } else if (!admin.getPasswordAdmin().equals(confirm)) {
            model.addAttribute("message", "Password and confirm are not match !");
            model.addAttribute("alert", "alert alert-danger");
            return view;
        }

        return "";
    }

    @GetMapping("/changePasswordView")
    public String changePasswordView(Model model, HttpSession session) {
        String checkLogin = checkLogin(session, model);
        if (checkLogin.length() > 0) {
            return checkLogin;
        }
        return "admin/changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(HttpSession session, Model model, HttpServletRequest request) {
        String checkLogin = checkLogin(session, model);
        if (checkLogin.length() > 0) {
            return checkLogin;
        }
        String currentPassword = request.getParameter("currentPassword");
        Admin admin = (Admin) session.getAttribute("admin");
        if (!bCryptPasswordEncoder.matches(currentPassword, admin.getPasswordAdmin())) {
            model.addAttribute("message", "Wrong password !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/changePassword";
        }
        String newPassword = request.getParameter("newPassword");
        if (newPassword.length() < 3) {
            model.addAttribute("message", "Password at least 3 letters !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/changePassword";
        }
        String confirmPassword = request.getParameter("confirmPassword");
        if (!confirmPassword.equals(newPassword)) {
            model.addAttribute("message", "Password and confirm password are not match !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/changePassword";
        }
        admin.setPasswordAdmin(bCryptPasswordEncoder.encode(newPassword));
        adminService.save(admin);
        session.setAttribute("admin", admin);
        model.addAttribute("message", "Change password successfully !");
        model.addAttribute("alert", "alert alert-success");
        return "admin/changePassword";
    }

    @GetMapping("/removeAdmin/{idAdmin}")
    public String removeAdmin(@PathVariable(value = "idAdmin") Long idAdmin, Model model, HttpSession session) {
        String checkLogin = checkLogin(session, model);
        if (checkLogin.length() > 0) {
            return checkLogin;
        }
        Long id = (idAdmin * 2 - 74) / 4;
        if (((Admin) session.getAttribute("admin")).getIdAdmin() == id) {
            model.addAttribute("message", "Can not delete yourself !");
            model.addAttribute("alert", "alert alert-danger");
            model.addAttribute("adminList", adminService.findAll());
            return "admin/adminList";
        }
        Admin admin = adminService.findById(id);
        admin.setStatus("REMOVED");
        adminService.save(admin);
        model.addAttribute("message", "Delete Admin Successful !");
        model.addAttribute("alert", "alert alert-success");
        model.addAttribute("adminList", adminService.findAll());
        return "admin/adminList";
    }

    @RequestMapping("/setAdmin/{idAdmin}")
    public String setAdmin(@PathVariable(value = "idAdmin") Long idAdmin, HttpSession session, Model model) {
        String checkLogin = checkLogin(session, model);
        if (checkLogin.length() > 0) {
            return checkLogin;
        }
        Long id = (idAdmin * 2 - 74) / 4;
        Admin admin = adminService.findById(id);
        admin.setRole(true);
        admin.setStatus("ACTIVED");
        adminService.save(admin);
        model.addAttribute("message", "Set Admin Successful !");
        model.addAttribute("alert", "alert alert-success");
        model.addAttribute("adminList", adminService.findAll());
        return "admin/adminList";
    }

    public Admin checkCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Admin admin = new Admin();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("usernameAdmin")) {
                admin.setUsernameAdmin(cookie.getValue());
            }
            if (cookie.getName().equalsIgnoreCase("passwordAdmin")) {
                admin.setPasswordAdmin(cookie.getValue());
            }
        }
        return admin;
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
