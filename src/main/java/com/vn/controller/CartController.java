package com.vn.controller;

import com.vn.model.Cart_detail;
import com.vn.model.Customer;
import com.vn.model.Product;
import com.vn.service.CartDetailService;
import com.vn.service.CartService;
import com.vn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

@Controller
public class CartController {

    @Autowired
    private CartDetailService cartDetailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/cart")
    public String cartView(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "loginUser";
        }
        model.addAttribute("customer", customer);

        TreeMap<Product, Integer> productTreeMap = new TreeMap<Product, Integer>(
                new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return o1.getIdProduct().compareTo(o2.getIdProduct());
                    }
                }
        );

        List<Cart_detail> cart_details = cartDetailService.findByCustomer(customer.getIdCustomer());
        for (Cart_detail cart_detail : cart_details) {
            productTreeMap.put(cart_detail.getIdProduct(), cart_detail.getQuantity());
        }

        model.addAttribute("entry", productTreeMap.entrySet());
        model.addAttribute("idCart", cartService.findIdCart(customer.getIdCustomer()));

        return "cart";
    }

    @RequestMapping("/addToCart/{idProduct}")
    public String addToCart(@PathVariable(name = "idProduct") Long idProduct, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "loginUser";
        }
        model.addAttribute("customer", customer);

        Long id = (idProduct * 2 - 74) / 4;

        List<Cart_detail> cart_details = cartDetailService.findByCustomer(customer.getIdCustomer());

        for (Cart_detail cart_detail : cart_details) {
            if (cart_detail.getIdProduct().getIdProduct() == id) {
                cart_detail.setQuantity(cart_detail.getQuantity() + 1);
                cartDetailService.update(cart_detail);
                return cartView(session, model);
            }
        }
        Cart_detail cart_detail = new Cart_detail();
        cart_detail.setQuantity(1);
        cart_detail.setIdProduct(productService.findById(id));
        cart_detail.setIdCart(cartService.findIdCart(customer.getIdCustomer()));
        cartDetailService.save(cart_detail);

        return cartView(session, model);
    }

    @RequestMapping("/plusProduct/{idProduct}")
    public String plusProduct(@PathVariable(name = "idProduct") Long idProduct, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");

        List<Cart_detail> cart_details = cartDetailService.findByCustomer(customer.getIdCustomer());

        Long idP = (idProduct * 2 - 74) / 4;

        for (Cart_detail cart_detail : cart_details) {
            if (cart_detail.getIdProduct().getIdProduct() == idP) {
                cart_detail.setQuantity(cart_detail.getQuantity() + 1);
                cartDetailService.update(cart_detail);
                return cartView(session, model);
            }
        }

        return cartView(session, model);
    }

    @RequestMapping("/minusProduct/{idProduct}")
    public String minusProduct(@PathVariable(name = "idProduct") Long idProduct, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");

        List<Cart_detail> cart_details = cartDetailService.findByCustomer(customer.getIdCustomer());

        Long idP = (idProduct * 2 - 74) / 4;

        for (Cart_detail cart_detail : cart_details) {
            if (cart_detail.getIdProduct().getIdProduct() == idP) {
                cart_detail.setQuantity(cart_detail.getQuantity() - 1);
                cartDetailService.update(cart_detail);
                if (cart_detail.getQuantity() == 0) {
                    return removeProduct(idProduct, (cartService.findIdCart(customer.getIdCustomer()).getIdCart() * 4 + 74) / 2, session, model);
                }
                return cartView(session, model);
            }
        }

        return cartView(session, model);
    }

    @RequestMapping("/removeProduct/{idProduct}/{idCart}")
    public String removeProduct(@PathVariable(name = "idProduct") Long idProduct, @PathVariable(name = "idCart") Long idCart, HttpSession session, Model model) {
        Long idP = (idProduct * 2 - 74) / 4;
        Long idC = (idCart * 2 - 74) / 4;

        cartDetailService.removeProduct(idC, idP);
        model.addAttribute("message", "Remove Successfully !");
        model.addAttribute("alert", "alert alert-success");

        return cartView(session, model);
    }
}
