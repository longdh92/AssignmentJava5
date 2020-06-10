package com.vn.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "invoice_detail")
public class Invoice_detail implements Serializable {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idInvoice")
    @Id
    private Invoice idInvoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idProduct")
    @Id
    private Product idProduct;

    @Column(name = "quantity")
    private int quantity;

    public Invoice getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(Invoice idInvoice) {
        this.idInvoice = idInvoice;
    }

    public Product getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Product idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
