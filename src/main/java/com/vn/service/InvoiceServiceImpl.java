package com.vn.service;

import com.vn.model.Customer;
import com.vn.model.Invoice;
import com.vn.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice findById(Long id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public void save(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    @Override
    public void remove(Long id) {
        invoiceRepository.remove(id);
    }

    @Override
    public List<Invoice> findByCustomer(Customer customer) {
        return invoiceRepository.findByCustomer(customer);
    }
}
