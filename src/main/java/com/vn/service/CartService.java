package com.vn.service;

import com.vn.model.Cart;

import java.util.List;

public interface CartService {
    List<Cart> findAll();

    Cart findById(Long id);

    void save(Cart cart);

    void remove(Long id);

    Cart findIdCart(Long idCustomer);
}
