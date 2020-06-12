package com.vn.repository;

import com.vn.model.Cart;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CartRepositoryImpl implements CartRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Cart> findAll() {
        String query = "select c from Cart c";
        TypedQuery<Cart> cartTypedQuery = entityManager.createQuery(query, Cart.class);
        return cartTypedQuery.getResultList();
    }

    @Override
    public Cart findById(Long id) {
        return entityManager.find(Cart.class, id);
    }

    @Override
    public void save(Cart model) {
        if (model.getIdCart() != null) {
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
    public void update(Cart model) {

    }

    @Override
    public Cart findByName(String name) {
        return null;
    }

    @Override
    public Cart findIdCart(Long idCustomer) {
        String query = "select c from Cart c where c.customer.idCustomer = :idCustomer";
        TypedQuery<Cart> cartTypedQuery = entityManager.createQuery(query, Cart.class);
        cartTypedQuery.setParameter("idCustomer", idCustomer);
        return cartTypedQuery.getResultList().get(0);
    }
}
