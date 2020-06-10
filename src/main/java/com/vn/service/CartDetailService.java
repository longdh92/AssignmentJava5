package com.vn.service;

import com.vn.model.Cart_detail;

import java.util.List;

public interface CartDetailService {
    List<Cart_detail> findAll();

    Cart_detail findById(Long id);

    void save(Cart_detail cart_detail);

    void remove(Long id);
}
