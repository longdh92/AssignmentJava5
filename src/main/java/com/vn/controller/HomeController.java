package com.vn.controller;

import com.vn.service.CategoryService;
import com.vn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("productList", productService.findAll());
        model.addAttribute("categoryList", categoryService.findAll());
        return "index";
    }
}
