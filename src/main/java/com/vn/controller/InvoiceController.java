package com.vn.controller;

import com.vn.model.*;
import com.vn.service.InvoiceDetailService;
import com.vn.service.InvoiceService;
import com.vn.service.InvoiceStatusService;
import com.vn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceDetailService invoiceDetailService;

    @Autowired
    private InvoiceStatusService invoiceStatusService;

    @Autowired
    private ProductService productService;

    @RequestMapping("/invoiceListView")
    public String invoiceList(HttpSession session, Model model,
                              @RequestParam(defaultValue = "") String status) {
        String checkLogin = checkLogin(session, model);
        if (checkLogin.length() > 0) {
            return checkLogin;
        }
        model.addAttribute("invoices", invoiceService.findByStatus(status));
        return "admin/invoiceList";
    }

    @RequestMapping("/invoiceDetail/{idInvoice}")
    public String invoiceDetail(@PathVariable(name = "idInvoice") Long idInvoice, HttpSession session, Model model) {
        String checkLogin = checkLogin(session, model);
        if (checkLogin.length() > 0) {
            return checkLogin;
        }
        Long id = (idInvoice * 2 - 74) / 4;
        List<Invoice_detail> detailList = invoiceDetailService.findByIdInvoice(id);
        model.addAttribute("detailList", detailList);
        model.addAttribute("invoice", invoiceService.findById(id));
        model.addAttribute("allStatus", invoiceStatusService.findAll());
        System.out.println(invoiceStatusService.findAll().size());
        return "admin/invoiceDetail";
    }

    @RequestMapping("/updateInvoice/{idInvoice}/{idStatus}")
    public String updateInvoice(@PathVariable(name = "idInvoice") Long idInvoice, @PathVariable(name = "idStatus") Long idStatus, HttpSession session, Model model) {
        String checkLogin = checkLogin(session, model);
        if (checkLogin.length() > 0) {
            return checkLogin;
        }
        Long id = (idInvoice * 2 - 74) / 4;
        Invoice invoice = invoiceService.findById(id);
        invoice.setInvoiceStatus(invoiceStatusService.findById(idStatus));
        invoiceService.save(invoice);

        if (invoiceStatusService.findById(idStatus).getStatusName().equalsIgnoreCase("Canceled")) {
            List<Invoice_detail> invoice_details = invoiceDetailService.findByIdInvoice(id);
            for (Invoice_detail invoice_detail : invoice_details) {
                Product product = invoice_detail.getIdProduct();
                product.setAmount(product.getAmount() + invoice_detail.getQuantity());
                productService.save(product);
            }
        }

        model.addAttribute("detailList", invoiceDetailService.findByIdInvoice(id));
        model.addAttribute("invoice", invoice);
        model.addAttribute("allStatus", invoiceStatusService.findAll());
        model.addAttribute("message", "Update Successful !");
        model.addAttribute("alert", "alert alert-success");
        return "admin/invoiceDetail";
    }

    public String checkLogin(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "admin/login";
        } else if (!admin.isRole() || !admin.getStatus().equalsIgnoreCase("ACTIVED")) {
            model.addAttribute("admin", admin);
            return "admin/admin";
        }
        model.addAttribute("admin", admin);
        return "";
    }
}
