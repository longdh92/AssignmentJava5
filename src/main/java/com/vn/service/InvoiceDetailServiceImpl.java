package com.vn.service;

import com.vn.model.Invoice_detail;
import com.vn.repository.InvoiceDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InvoiceDetailServiceImpl implements InvoiceDetailService {

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Override
    public List<Invoice_detail> findAll() {
        return invoiceDetailRepository.findAll();
    }

    @Override
    public Invoice_detail findById(Long id) {
        return invoiceDetailRepository.findById(id);
    }

    @Override
    public void save(Invoice_detail invoice_detail) {
        invoiceDetailRepository.save(invoice_detail);
    }

    @Override
    public void remove(Long id) {
        invoiceDetailRepository.remove(id);
    }
}
