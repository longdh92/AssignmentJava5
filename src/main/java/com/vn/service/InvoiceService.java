package com.vn.service;

import com.vn.model.Customer;
import com.vn.model.Invoice;

import java.util.List;

public interface InvoiceService {
    List<Invoice> findAll();

    Invoice findById(Long id);

    void save(Invoice invoice);

    void remove(Long id);

    List<Invoice> findByCustomer(Customer customer);
}
