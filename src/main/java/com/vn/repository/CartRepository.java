package com.vn.repository;

import com.vn.model.Cart;

public interface CartRepository extends Repository<Cart> {
    Cart findIdCart(Long idCustomer);
}
