package com.vn.controller;

import com.vn.model.Customer;
import com.vn.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customerList")
    public String customerList(Model model) {
        model.addAttribute("customerList", customerService.findAll());
        return "admin/customerList";
    }
}
