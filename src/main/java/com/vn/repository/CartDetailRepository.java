package com.vn.repository;

import com.vn.model.Cart_detail;

import java.util.List;

public interface CartDetailRepository extends Repository<Cart_detail> {
    List<Cart_detail> findByCustomer(Long idCustomer);
    void removeProduct(Long idCart, Long idProduct);
}
