package com.vn.controller;

import com.vn.model.Customer;
import com.vn.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private CartDetailService cartDetailService;

    @RequestMapping("/cart")
    public String cartView(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "loginUser";
        }
        model.addAttribute("customer", customer);
        return "cart";
    }

    @RequestMapping("/addToCart/{idProduct}")
    public String addToCart(@PathVariable(name = "idProduct") Long idProduct, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "loginUser";
        }
        model.addAttribute("customer", customer);



        return "cart";
    }
}
