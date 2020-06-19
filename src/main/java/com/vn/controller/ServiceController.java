package com.vn.controller;

import com.vn.model.Customer;
import com.vn.model.Invoice;
import com.vn.model.Invoice_detail;
import com.vn.model.Product;
import com.vn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

@Controller
public class ServiceController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceStatusService invoiceStatusService;

    @Autowired
    private InvoiceDetailService invoiceDetailService;

    @Autowired
    private ProductService productService;

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

    @RequestMapping("/cancelOrder/{idInvoice}")
    public String cancelOrder(@CookieValue(name = "emailCustomer", defaultValue = "") String emailCustomer,
                              @CookieValue(name = "passwordCustomer", defaultValue = "") String passwordCustomer,
                              @PathVariable(name = "idInvoice") Long idInvoice, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            model.addAttribute("customer", new Customer());
            model.addAttribute("emailCustomer", emailCustomer);
            model.addAttribute("passwordCustomer", passwordCustomer);
            return "loginUser";
        } else {
            model.addAttribute("customer", customer);
        }

        Long id = (idInvoice * 2 - 74) / 4;
        Invoice invoice = invoiceService.findById(id);
        invoice.setInvoiceStatus(invoiceStatusService.findById((long) 5));
        invoiceService.save(invoice);

        List<Invoice_detail> invoice_details = invoiceDetailService.findByIdInvoice(id);
        for (Invoice_detail invoice_detail : invoice_details) {
            Product product = invoice_detail.getIdProduct();
            product.setAmount(product.getAmount() + invoice_detail.getQuantity());
            productService.save(product);
        }

        TreeMap<List<Invoice_detail>, Integer> invoiceTreeMap = getInvoiceTreeMap(customer);
        model.addAttribute("invoiceTreeMap", invoiceTreeMap);

        return "historyOrder";
    }

    public TreeMap<List<Invoice_detail>, Integer> getInvoiceTreeMap(Customer customer) {
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
        return invoiceTreeMap;
    }
}
