package com.vn.service;

import com.vn.model.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> findAllAdmin();

    void save(Admin admin);

    Admin findByID(int id);

    void updateAdmin(Admin admin);

    void removeAdmin(Admin admin);
}
