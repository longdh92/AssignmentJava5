package com.vn.repository;

import com.vn.model.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class ProductRepositoryImpl implements ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Product> findAll() {
        String query = "select p from Product p";
        TypedQuery<Product> productTypedQuery = entityManager.createQuery(query, Product.class);
        return productTypedQuery.getResultList();
    }

    @Override
    public Product findById(Long id) {
        return entityManager.find(Product.class, id);
    }

    @Override
    public void save(Product model) {
        if (model.getIdProduct() != null) {
            // Update theo ID
            entityManager.merge(model);
        } else {
            entityManager.persist(model);
        }
    }

    @Override
    public void remove(Long id) {
        entityManager.remove(this.findById(id));
    }

    @Override
    public void update(Product model) {

    }

    @Override
    public Product findByName(String name) {
        return null;
    }
}
