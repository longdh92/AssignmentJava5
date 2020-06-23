package com.vn.controller;

import com.vn.model.Admin;
import com.vn.model.Customer;
import com.vn.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.SecureRandom;

@Controller
@RequestMapping("/admin")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    static final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom secureRandom = new SecureRandom();

    String randomString(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(characters.charAt(secureRandom.nextInt(characters.length())));
        }
        return stringBuilder.toString();
    }

    @GetMapping("/customerList")
    public String customerList(HttpSession session, Model model) {
        String checkLogin = checkLogin(session, model);
        if (checkLogin.length() > 0) {
            return checkLogin;
        }
        model.addAttribute("customerList", customerService.findAll());
        return "admin/customerList";
    }

    @RequestMapping("/resetPasswordCustomer/{idCustomer}")
    public String resetPasswordCustomer(@PathVariable(name = "idCustomer") Long idCustomer, HttpSession session, Model model) {
        String checkLogin = checkLogin(session, model);
        if (checkLogin.length() > 0) {
            return checkLogin;
        }
        Long id = (idCustomer * 2 - 74) / 4;
        Customer customer = customerService.findById(id);

        String newPassword = randomString(6);

        customer.setPasswordCustomer(bCryptPasswordEncoder.encode(newPassword));
        customer.setStatus("");
        customerService.save(customer);

        System.out.println("Sending email...");
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(customer.getEmailCustomer());
        message.setSubject("Reset password");
        message.setText("Your password is reset to: " + newPassword);

        // Send Message!
        javaMailSender.send(message);

        model.addAttribute("message", "Reset password successfully !");
        model.addAttribute("alert", "alert alert-success");
        return customerList(session, model);
    }

    private String checkLogin(HttpSession session, Model model) {
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
