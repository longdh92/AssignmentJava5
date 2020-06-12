package com.vn.repository;

import com.vn.model.Customer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CustomerRepositoryImpl implements CustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Customer> findAll() {
        String query = "select c from Customer c";
        TypedQuery<Customer> customerTypedQuery = entityManager.createQuery(query, Customer.class);
        return customerTypedQuery.getResultList();
    }

    @Override
    public Customer findById(Long id) {
        return entityManager.find(Customer.class, id);
    }

    @Override
    public void save(Customer model) {
        if (model.getIdCustomer() != null) {
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
    public void update(Customer model) {

    }

    @Override
    public Customer findByName(String name) {
        return null;
    }
}
