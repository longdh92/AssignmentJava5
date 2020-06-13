package com.vn.controller;

import com.vn.model.Customer;
import com.vn.service.CategoryService;
import com.vn.service.CustomerService;
import com.vn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/home")
    public String home(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            model.addAttribute("customer", new Customer());
        } else {
            model.addAttribute("customer", customer);
        }
        model.addAttribute("productList", productService.findAll());
        model.addAttribute("categoryList", categoryService.findAll());
        return "index";
    }

    @RequestMapping("/loginCustomerView")
    public String loginCustomerView(@CookieValue(name = "emailCustomer", defaultValue = "") String emailCustomer,
                                    @CookieValue(name = "passwordCustomer", defaultValue = "") String passwordCustomer,
                                    Model model,
                                    HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            model.addAttribute("customer", new Customer());
        } else {
            model.addAttribute("customer", customer);
        }
        model.addAttribute("emailCustomer", emailCustomer);
        model.addAttribute("passwordCustomer", passwordCustomer);
        return "loginUser";
    }

    @PostMapping("/loginCustomer")
    public String loginCustomer(Customer customer, HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) {
        List<Customer> customerList = customerService.findAll();
        for (Customer customer1 : customerList) {
            if (customer1.getEmailCustomer().equalsIgnoreCase(customer.getEmailCustomer().trim())) {
                if (customer1.getPasswordCustomer().equals(customer.getPasswordCustomer())) {
                    Cookie emailCustomer = new Cookie("emailCustomer", customer1.getEmailCustomer());
                    Cookie passwordCustomer = new Cookie("passwordCustomer", customer1.getPasswordCustomer());
                    if (request.getParameter("rememberCustomer") != null) {
                        emailCustomer.setMaxAge(3600);
                        passwordCustomer.setMaxAge(3600);
                    } else {
                        emailCustomer.setMaxAge(0);
                        passwordCustomer.setMaxAge(0);
                    }
                    response.addCookie(emailCustomer);
                    response.addCookie(passwordCustomer);
                    session.setAttribute("customer", customer1);
                    return home(model, session);
                } else {
                    model.addAttribute("message", "Wrong password !");
                    model.addAttribute("alert", "alert alert-danger");
                    return "/loginUser";
                }
            } else {
                continue;
            }
        }
        model.addAttribute("message", "Can not find this email address !");
        model.addAttribute("alert", "alert alert-danger");
        return "/loginUser";
    }

    @RequestMapping("/logoutCustomer")
    public String logout(HttpServletRequest request, HttpSession session, Model model) {
        Customer customer = checkCookie(request);
        model.addAttribute("emailCustomer", customer.getEmailCustomer());
        model.addAttribute("passwordCustomer", customer.getPasswordCustomer());
        session.removeAttribute("customer");
        model.addAttribute("customer", new Customer());
        return "loginUser";
    }

    public Customer checkCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Customer customer = new Customer();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("emailCustomer")) {
                customer.setEmailCustomer(cookie.getValue());
            }
            if (cookie.getName().equalsIgnoreCase("passwordCustomer")) {
                customer.setPasswordCustomer(cookie.getValue());
            }
        }
        if (customer.getEmailCustomer() == null) {
            customer.setEmailCustomer("");
        }
        return customer;
    }
}
