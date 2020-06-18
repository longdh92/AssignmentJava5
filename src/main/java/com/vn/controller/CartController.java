package com.vn.controller;

import com.vn.model.*;
import com.vn.repository.InvoiceStatusRepository;
import com.vn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.tree.Tree;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class CartController {

    @Autowired
    private CartDetailService cartDetailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceDetailService invoiceDetailService;

    @Autowired
    private InvoiceStatusRepository invoiceStatusRepository;

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
            model.addAttribute("customer", new Customer());
            model.addAttribute("emailCustomer", "");
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

    @PostMapping("/checkout/{total}")
    public String checkout(@CookieValue(name = "emailCustomer", defaultValue = "") String emailCustomer,
                           @CookieValue(name = "passwordCustomer", defaultValue = "") String passwordCustomer,
                           HttpSession session, Model model, Customer customer1,
                           @PathVariable(name = "total", required = false) double total) {
        Customer customer = (Customer) session.getAttribute("customer");
        List<Cart_detail> cart_details = cartDetailService.findByCustomer(customer.getIdCustomer());
        if (cart_details.size() == 0) {
            model.addAttribute("message", "Empty Cart !");
            model.addAttribute("alert", "alert alert-danger");
            return cartAndCheckout(emailCustomer, passwordCustomer, session, model, "checkout");
        }
        String validateCheckout = validateCheckout(model, customer1.getEmailCustomer(), customer1.getCustomerName(), customer1.getPhone(), "checkout");
        if (validateCheckout.length() > 0) {
            return cartAndCheckout(emailCustomer, passwordCustomer, session, model, "checkout");
        }

        Invoice invoice = new Invoice();
        invoice.setAddress(customer1.getAddress());
        invoice.setCustomerName(customer1.getCustomerName());
        invoice.setDate(new Date());
        invoice.setEmailCustomer(customer1.getEmailCustomer());
        invoice.setPhone(customer1.getPhone());
        invoice.setTotal(total);
        invoice.setIdCustomer(customer);
        invoice.setInvoiceStatus(invoiceStatusRepository.findByName("Wait for Confirmation"));
        invoiceService.save(invoice);

        for (Cart_detail cart_detail : cart_details) {
            Invoice_detail invoice_detail = new Invoice_detail();
            invoice_detail.setQuantity(cart_detail.getQuantity());
            invoice_detail.setIdInvoice(invoice);
            invoice_detail.setIdProduct(cart_detail.getIdProduct());
            invoiceDetailService.save(invoice_detail);
        }

        Long idCart = cartService.findIdCart(customer.getIdCustomer()).getIdCart();
        cartDetailService.removeCart(idCart);

        model.addAttribute("message", "Order Completed Successfully !");
        model.addAttribute("alert", "alert alert-success");

        return cartAndCheckout(emailCustomer, passwordCustomer, session, model, "checkout");
    }

    public String validateCheckout(Model model, String emailCustomer, String customerName, String phone, String view) {
        if (!emailCustomer.matches("\\w+@\\w+(\\.\\w+){1,2}")) {
            model.addAttribute("message", "Invalid Email Address !");
            model.addAttribute("alert", "alert alert-danger");
            return view;
        }
        if (!customerName.matches("^[a-zA-Z\\s\\p{L}]+")) {
            model.addAttribute("message", "Only Alphabet and White Space Characters !");
            model.addAttribute("alert", "alert alert-danger");
            return view;
        }
        if (!phone.matches("0\\d{9}")) {
            model.addAttribute("message", "Invalid Phone Number !");
            model.addAttribute("alert", "alert alert-danger");
            return view;
        }
        return "";
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

    @RequestMapping("/historyView")
    public String historyView(@CookieValue(name = "emailCustomer", defaultValue = "") String emailCustomer,
                              @CookieValue(name = "passwordCustomer", defaultValue = "") String passwordCustomer,
                              HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        String checkLogin = checkLogin(emailCustomer, passwordCustomer, session, model);
        if (checkLogin.length() > 0) {
            return checkLogin;
        }

        TreeMap<List<Invoice_detail>, Integer> invoiceTreeMap = new TreeMap<>(new Comparator<List<Invoice_detail>>() {
            @Override
            public int compare(List<Invoice_detail> o1, List<Invoice_detail> o2) {
                return o2.get(0).getIdInvoice().getDate().compareTo(o1.get(0).getIdInvoice().getDate());
            }
        });

        List<Invoice> invoices = invoiceService.findByCustomer(customer);
        List<List<Invoice_detail>> invoiceList = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoiceList.add(invoiceDetailService.findByIdInvoice(invoice.getIdInvoice()));
        }
        for (List<Invoice_detail> invoice_details : invoiceList) {
            int total = 0;
            for (Invoice_detail invoice_detail : invoice_details) {
                total += invoice_detail.getQuantity() * invoice_detail.getIdProduct().getPrice();
            }
            invoiceTreeMap.put(invoice_details, total);
        }
        model.addAttribute("invoiceTreeMap", invoiceTreeMap);
        return "historyOrder";
    }

    public String cartAndCheckout(@CookieValue(name = "emailCustomer", defaultValue = "") String emailCustomer,
                                  @CookieValue(name = "passwordCustomer", defaultValue = "") String passwordCustomer,
                                  HttpSession session, Model model, String view) {
        Customer customer = (Customer) session.getAttribute("customer");
        String checkLogin = checkLogin(emailCustomer, passwordCustomer, session, model);
        if (checkLogin.length() > 0) {
            return checkLogin;
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

    public String checkLogin(@CookieValue(name = "emailCustomer", defaultValue = "") String emailCustomer,
                             @CookieValue(name = "passwordCustomer", defaultValue = "") String passwordCustomer,
                             HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            model.addAttribute("customer", new Customer());
            model.addAttribute("emailCustomer", emailCustomer);
            model.addAttribute("passwordCustomer", passwordCustomer);
            return "loginUser";
        } else {
            model.addAttribute("customer", customer);
        }
        return "";
    }
}
