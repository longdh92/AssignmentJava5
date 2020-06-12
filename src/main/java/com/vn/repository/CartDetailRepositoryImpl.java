package com.vn.repository;

import com.vn.model.Cart_detail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CartDetailRepositoryImpl implements CartDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Cart_detail> findAll() {
        String query = "select c from Cart_detail c";
        TypedQuery<Cart_detail> cart_detailTypedQuery = entityManager.createQuery(query, Cart_detail.class);
        return cart_detailTypedQuery.getResultList();
    }

    @Override
    public Cart_detail findById(Long id) {
        return entityManager.find(Cart_detail.class, id);
    }

    @Override
    public void save(Cart_detail model) {
        entityManager.persist(model);

    }

    @Override
    public void remove(Long id) {
        entityManager.remove(this.findById(id));
    }

    @Override
    public void update(Cart_detail model) {
        entityManager.merge(model);
    }

    @Override
    public Cart_detail findByName(String name) {
        return null;
    }

    @Override
    public List<Cart_detail> findByCustomer(Long idCustomer) {
        String query = "select cd from Cart_detail cd, Cart c where c.customer.idCustomer = :idCustomer and c.idCart = cd.idCart.idCart";
        TypedQuery<Cart_detail> cart_detailTypedQuery = entityManager.createQuery(query, Cart_detail.class);
        cart_detailTypedQuery.setParameter("idCustomer", idCustomer);
        return cart_detailTypedQuery.getResultList();
    }

    @Override
    public void removeProduct(Long idCart, Long idProduct) {
        String hql = "delete from Cart_detail where idCart.idCart = :idCart and idProduct.idProduct = :idProduct";
        Query query = entityManager.createQuery(hql);
        query.setParameter("idCart", idCart);
        query.setParameter("idProduct", idProduct);
        query.executeUpdate();
    }

}
