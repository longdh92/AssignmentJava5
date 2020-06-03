package com.vn.controller;

import com.vn.model.Category;
import com.vn.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categoryList")
    public String categoryList(Model model) {
        model.addAttribute("categoryList", categoryService.findAll());
        return "admin/categoryList";
    }

    @PostMapping("/addNewCategory")
    public String addCategory(Model model, Category category) {
        List<Category> categoryList = categoryService.findAll();
        for (Category category1 : categoryList) {
            if (category.getCategoryName().trim().equalsIgnoreCase(category1.getCategoryName())) {
                model.addAttribute("message", "The category already exists !");
                model.addAttribute("alert", "alert alert-danger");
                return categoryList(model);
            }
        }
        categoryService.save(category);
        model.addAttribute("message", "Add new category successful !");
        model.addAttribute("alert", "alert alert-success");
        return categoryList(model);
    }
}
