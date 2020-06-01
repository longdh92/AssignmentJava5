package com.vn.service;

import com.vn.model.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> findAll();

    Admin findById(Long id);

    void save(Admin customer);

    void remove(Long id);
}
