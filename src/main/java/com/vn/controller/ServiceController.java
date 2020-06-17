package com.vn.controller;

import com.vn.model.Customer;
import com.vn.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ServiceController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/resetPasswordView")
    public String resetPasswordView(Model model) {
        model.addAttribute("customer", new Customer());
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(Model model, Customer customer) {
        model.addAttribute("customer", new Customer());
        Customer customer1 = customerService.findByEmail(customer.getEmailCustomer());
        if (customer1 == null) {
            model.addAttribute("message", "This email does not register yet !");
            model.addAttribute("alert", "alert alert-danger");
        } else {
            customer1.setStatus("Reset Password");
            customerService.save(customer1);
            model.addAttribute("message", "Send Request successfully !");
            model.addAttribute("alert", "alert alert-success");
        }
        return "resetPassword";
    }
}
