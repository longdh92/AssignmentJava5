package com.vn.controller;

import com.vn.model.Admin;
import com.vn.model.Category;
import com.vn.model.Product;
import com.vn.service.CategoryService;
import com.vn.service.ProductService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
@MultipartConfig
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    ServletContext context;

    @GetMapping("/productList")
    public String productList(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "admin/login";
        }
        model.addAttribute("admin", admin);
        model.addAttribute("productList", productService.findAll());
        return "admin/productList";
    }

    @GetMapping("/insertUpdateProductView/{idProduct}")
    public String editProduct(@PathVariable(value = "idProduct") Long idProduct, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "admin/login";
        }
        model.addAttribute("admin", admin);
        Long id = (idProduct * 2 - 74) / 4;
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("categoryList", categoryService.findAll());
        return "admin/insertUpdateProduct";
    }

    @PostMapping("/insertUpdateProduct")
    public String saveProduct(Product product, Model model, HttpSession session, @RequestParam("categoryName") String categoryName,
                              @RequestParam("imageProduct") MultipartFile photo) throws IOException {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "admin/login";
        }
        model.addAttribute("admin", admin);

        String check = validateProduct(model, product, photo);
        if (check.length() > 0) {
            return check;
        }

        Category category = categoryService.findByName(categoryName);
        product.setIdCategory(category);

        if (photo.getOriginalFilename().length() > 0) {
            product.setImage(photo.getOriginalFilename());
        }

        String path1 = "D:\\JavaEE\\AssignmentJava5\\src\\main\\webapp\\WEB-INF\\views\\admin\\resourcesAdmin\\images\\home\\" + product.getImage();
        String path2 = context.getRealPath("\\WEB-INF\\views\\admin\\resourcesAdmin\\images\\home" + File.separator + product.getImage());

        FileUtils.copyInputStreamToFile(photo.getInputStream(), new File(path1));
        FileUtils.copyInputStreamToFile(photo.getInputStream(), new File(path2));

        productService.save(product);

        if (product.getIdProduct() != null) {
            model.addAttribute("product", new Product());
        } else {
            model.addAttribute("product", product);
        }
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("message", "Successful !");
        model.addAttribute("alert", "alert alert-success");
        return "admin/insertUpdateProduct";
    }

    public String validateProduct(Model model, Product product, MultipartFile photo) {
        if (product.getProductName().trim().length() == 0) {
            model.addAttribute("message", "Empty product name !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/insertUpdateProduct";
        }
        if (product.getPrice() <= 0) {
            model.addAttribute("message", "Price must be higher than 0 !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/insertUpdateProduct";
        }
        if (product.getAmount() <= 0) {
            model.addAttribute("message", "Amount must be higher than 0 !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/insertUpdateProduct";
        }
        if (product.getIdProduct() == null && photo.getOriginalFilename().length() == 0) {
            model.addAttribute("message", "Choose an image !");
            model.addAttribute("alert", "alert alert-danger");
            return "admin/insertUpdateProduct";
        }
        return "";
    }
}
