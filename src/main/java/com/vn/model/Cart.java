package com.vn.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "cart")
public class Cart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCart")
    private Long idCart;

    @OneToOne
    @JoinColumn(name = "idCustomer")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCart")
    private Set<Cart_detail> cart_details;

    public Long getIdCart() {
        return idCart;
    }

    public void setIdCart(Long idCart) {
        this.idCart = idCart;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<Cart_detail> getCart_details() {
        return cart_details;
    }

    public void setCart_details(Set<Cart_detail> cart_details) {
        this.cart_details = cart_details;
    }
}
