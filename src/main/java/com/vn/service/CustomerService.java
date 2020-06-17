package com.vn.service;

import com.vn.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();

    Customer findById(Long id);

    void save(Customer customer);

    void remove(Long id);

    Customer findByEmail(String emailCustomer);
}
