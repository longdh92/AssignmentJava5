package com.vn.service;

import com.vn.model.Invoice_detail;

import java.util.List;

public interface InvoiceDetailService {
    List<Invoice_detail> findAll();

    Invoice_detail findById(Long id);

    void save(Invoice_detail invoice_detail);

    void remove(Long id);

    List<Invoice_detail> findByIdInvoice(Long id);
}
