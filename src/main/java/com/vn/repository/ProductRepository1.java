package com.vn.repository;

import com.vn.model.Category;
import com.vn.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository1 extends JpaRepository<Product, Integer> {
    @Query("select p from Product p where p.idCategory.categoryName = ?1")
    Page<Product> findByCategory(String categoryName, Pageable pageable);

    @Query("select p from Product p where p.productName like %?1%")
    Page<Product> findByProductName(String productName, Pageable pageable);
}
