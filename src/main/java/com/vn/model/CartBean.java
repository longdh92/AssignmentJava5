package com.vn.model;

import java.util.Map;
import java.util.TreeMap;

public class CartBean {
    private TreeMap<Product, Integer> listProduct;

    public CartBean() {
        listProduct = new TreeMap<Product, Integer>();
    }

    public TreeMap<Product, Integer> getListProduct() {
        return listProduct;
    }

    public void setListProduct(TreeMap<Product, Integer> listProduct) {
        this.listProduct = listProduct;
    }

    public void insertToCart(Product product, int quantity) {
        boolean check = listProduct.containsKey(product);
        if (check) {
            int old_quantity = listProduct.get(product);
            quantity += old_quantity;
            listProduct.put(product, quantity);
        } else {
            listProduct.put(product, quantity);
        }
    }

    public void updateToCart(Product product, int quantity) {
        boolean check = listProduct.containsKey(product);
        if (check) {
            if (quantity <= 0) {
                listProduct.remove(product);
            } else {
                listProduct.remove(product);
                listProduct.put(product, quantity);
            }
        }
    }

    public void removeToCart(Product product, int quantity) {
        boolean check = listProduct.containsKey(product);
        if (check) {
            int old_quantity = listProduct.get(product);
            quantity = old_quantity - quantity;
            if (quantity <= 0) {
                listProduct.remove(product);
            } else {
                listProduct.remove(product);
                listProduct.put(product, quantity);
            }
        }
    }

    public void removeCart(Product product) {
        boolean check = listProduct.containsKey(product);
        if (check) {
            listProduct.remove(product);
        }
    }

    public int countItem() {
        int count = 0;
        for (Map.Entry<Product, Integer> listEntry : listProduct.entrySet()) {
            count += listEntry.getValue();
        }
        return count;
    }

    public int total() {
        int count = 0;
        for (Map.Entry<Product, Integer> listEntry : listProduct.entrySet()) {
            count += listEntry.getValue() * listEntry.getKey().getPrice();
        }
        return count;
    }
}
