package com.vn.repository;

import com.vn.model.Customer;
import com.vn.model.Invoice;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class InvoiceRepositoryImpl implements InvoiceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Invoice> findAll() {
        String query = "select i from Invoice i";
        TypedQuery<Invoice> invoiceTypedQuery = entityManager.createQuery(query, Invoice.class);
        return invoiceTypedQuery.getResultList();
    }

    @Override
    public Invoice findById(Long id) {
        return entityManager.find(Invoice.class, id);
    }

    @Override
    public void save(Invoice model) {
        if (model.getIdInvoice() != null) {
            // Update theo ID
            entityManager.merge(model);
        } else {
            entityManager.persist(model);
        }
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void update(Invoice model) {

    }

    @Override
    public Invoice findByName(String name) {
        return null;
    }

    @Override
    public List<Invoice> findByCustomer(Customer customer) {
        String query = "select i from Invoice i where i.idCustomer = :customer";
        TypedQuery<Invoice> invoiceTypedQuery = entityManager.createQuery(query, Invoice.class);
        invoiceTypedQuery.setParameter("customer", customer);
        return invoiceTypedQuery.getResultList();
    }
}
