package com.vn.repository;

import com.vn.model.Customer;
import com.vn.model.Invoice;

import java.util.List;

public interface InvoiceRepository extends Repository<Invoice> {
    List<Invoice> findByCustomer(Customer customer);
}
