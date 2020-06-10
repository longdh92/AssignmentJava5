package com.vn.service;

import com.vn.model.Cart_detail;
import com.vn.repository.CartDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CartDetailServiceImpl implements CartDetailService {

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Override
    public List<Cart_detail> findAll() {
        return cartDetailRepository.findAll();
    }

    @Override
    public Cart_detail findById(Long id) {
        return cartDetailRepository.findById(id);
    }

    @Override
    public void save(Cart_detail cart_detail) {
        cartDetailRepository.save(cart_detail);
    }

    @Override
    public void remove(Long id) {
        cartDetailRepository.remove(id);
    }
}
