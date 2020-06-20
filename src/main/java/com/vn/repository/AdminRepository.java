package com.vn.repository;

import com.vn.model.Admin;

import java.util.List;

public interface AdminRepository extends Repository<Admin> {
    List<Admin> findByRole(boolean role);
}
