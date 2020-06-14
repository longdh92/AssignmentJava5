package com.vn.controller;

import com.vn.model.Cart_detail;
import com.vn.model.Customer;
import com.vn.model.Product;
import com.vn.service.CartDetailService;
import com.vn.service.CartService;
import com.vn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class CartController {

    @Autowired
    private CartDetailService cartDetailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @RequestMapping("/cart")
    public String cartView(@CookieValue(name = "emailCustomer", defaultValue = "") String emailCustomer,
                           @CookieValue(name = "passwordCustomer", defaultValue = "") String passwordCustomer,
                           HttpSession session,
                           Model model) {
        return cartAndCheckout(emailCustomer, passwordCustomer, session, model, "cart");
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
                return cartView(customer.getEmailCustomer(), customer.getPasswordCustomer(), session, model);
            }
        }
        Cart_detail cart_detail = new Cart_detail();
        cart_detail.setQuantity(1);
        cart_detail.setIdProduct(productService.findById(id));
        cart_detail.setIdCart(cartService.findIdCart(customer.getIdCustomer()));
        cartDetailService.save(cart_detail);

        return cartView(customer.getEmailCustomer(), customer.getPasswordCustomer(), session, model);
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
                return cartView(customer.getEmailCustomer(), customer.getPasswordCustomer(), session, model);
            }
        }

        return cartView(customer.getEmailCustomer(), customer.getPasswordCustomer(), session, model);
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
                return cartView(customer.getEmailCustomer(), customer.getPasswordCustomer(), session, model);
            }
        }

        return cartView(customer.getEmailCustomer(), customer.getPasswordCustomer(), session, model);
    }

    @RequestMapping("/removeProduct/{idProduct}/{idCart}")
    public String removeProduct(@PathVariable(name = "idProduct") Long idProduct, @PathVariable(name = "idCart") Long idCart, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");

        Long idP = (idProduct * 2 - 74) / 4;
        Long idC = (idCart * 2 - 74) / 4;

        cartDetailService.removeProduct(idC, idP);
        model.addAttribute("message", "Remove Successfully !");
        model.addAttribute("alert", "alert alert-success");

        return cartView(customer.getEmailCustomer(), customer.getPasswordCustomer(), session, model);
    }

    @RequestMapping("/checkoutView")
    public String checkoutView(@CookieValue(name = "emailCustomer", defaultValue = "") String emailCustomer,
                               @CookieValue(name = "passwordCustomer", defaultValue = "") String passwordCustomer,
                               HttpSession session, Model model) {
        return cartAndCheckout(emailCustomer, passwordCustomer, session, model, "checkout");
    }

    @RequestMapping("/removeCart")
    public String removeCart(@CookieValue(name = "emailCustomer", defaultValue = "") String emailCustomer,
                             @CookieValue(name = "passwordCustomer", defaultValue = "") String passwordCustomer,
                             HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        Long idCart = cartService.findIdCart(customer.getIdCustomer()).getIdCart();
        cartDetailService.removeCart(idCart);
        model.addAttribute("message", "Remove Successfully !");
        model.addAttribute("alert", "alert alert-success");
        return cartView(emailCustomer, passwordCustomer, session, model);
    }

    public String cartAndCheckout(@CookieValue(name = "emailCustomer", defaultValue = "") String emailCustomer,
                                  @CookieValue(name = "passwordCustomer", defaultValue = "") String passwordCustomer,
                                  HttpSession session, Model model, String view) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            model.addAttribute("customer", new Customer());
            model.addAttribute("emailCustomer", emailCustomer);
            model.addAttribute("passwordCustomer", passwordCustomer);
            return "loginUser";
        } else {
            model.addAttribute("customer", customer);
        }

        TreeMap<Product, Integer> productTreeMap = new TreeMap<Product, Integer>(
                new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return o1.getIdProduct().compareTo(o2.getIdProduct());
                    }
                }
        );
        List<Cart_detail> cart_details = cartDetailService.findByCustomer(customer.getIdCustomer());
        double total = 0;
        for (Cart_detail cart_detail : cart_details) {
            productTreeMap.put(cart_detail.getIdProduct(), cart_detail.getQuantity());
            total += cart_detail.getQuantity() * cart_detail.getIdProduct().getPrice();
        }
        model.addAttribute("entry", productTreeMap.entrySet());
        model.addAttribute("idCart", cartService.findIdCart(customer.getIdCustomer()));
        model.addAttribute("total", total);

        return view;
    }
}
