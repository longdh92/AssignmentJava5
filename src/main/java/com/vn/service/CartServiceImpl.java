package com.vn.service;

import com.vn.model.Cart;
import com.vn.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id);
    }

    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public void remove(Long id) {
        cartRepository.remove(id);
    }

    @Override
    public Cart findIdCart(Long idCustomer) {
        return cartRepository.findIdCart(idCustomer);
    }
}
