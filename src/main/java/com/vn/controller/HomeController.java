package com.vn.controller;

import com.vn.model.Customer;
import com.vn.service.CartDetailService;
import com.vn.service.CategoryService;
import com.vn.service.CustomerService;
import com.vn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String home(Model model) {
        model.addAttribute("productList", productService.findAll());
        model.addAttribute("categoryList", categoryService.findAll());
        return "index";
    }

    @RequestMapping("/loginCustomerView")
    public String loginCustomerView() {
        return "loginUser";
    }

    @PostMapping("/loginCustomer")
    public String loginCustomer(Customer customer, HttpSession session, Model model) {
        List<Customer> customerList = customerService.findAll();
        for (Customer customer1 : customerList) {
            if (customer1.getEmailCustomer().equalsIgnoreCase(customer.getEmailCustomer().trim())) {
                if (customer1.getPasswordCustomer().equals(customer.getPasswordCustomer())) {
                    session.setAttribute("customer", customer1);
                    return home(model);
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
}
