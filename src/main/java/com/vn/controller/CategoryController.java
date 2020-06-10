package com.vn.controller;

import com.vn.model.Admin;
import com.vn.model.Category;
import com.vn.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categoryList")
    public String categoryList(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "admin/login";
        }
        model.addAttribute("admin", admin);
        model.addAttribute("categoryList", categoryService.findAll());
        return "admin/categoryList";
    }

    @PostMapping("/addNewCategory")
    public String addCategory(Model model, Category category, HttpSession session) {
        if (category.getCategoryName().trim().length() == 0) {
            model.addAttribute("message", "Empty Category Name !");
            model.addAttribute("alert", "alert alert-danger");
            return categoryList(session, model);
        }
        List<Category> categoryList = categoryService.findAll();
        for (Category category1 : categoryList) {
            if (category.getCategoryName().trim().equalsIgnoreCase(category1.getCategoryName())) {
                model.addAttribute("message", "The category already exists !");
                model.addAttribute("alert", "alert alert-danger");
                return categoryList(session, model);
            }
        }
        categoryService.save(category);
        model.addAttribute("message", "Add new category successful !");
        model.addAttribute("alert", "alert alert-success");
        return categoryList(session, model);
    }
}
