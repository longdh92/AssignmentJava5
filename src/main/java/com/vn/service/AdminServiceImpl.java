package com.vn.service;

import com.vn.model.Admin;
import com.vn.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin findById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    public void save(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public void remove(Long id) {
        adminRepository.remove(id);
    }

    @Override
    public Admin findByName(String name) {
        return adminRepository.findByName(name);
    }

    @Override
    public List<Admin> findByRole(boolean role) {
        return adminRepository.findByRole(role);
    }
}
