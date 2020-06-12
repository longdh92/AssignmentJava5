package com.vn.repository;

import com.vn.model.Invoice_detail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class InvoiceDetailRepositoryImpl implements InvoiceDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Invoice_detail> findAll() {
        String query = "select i from Invoice_detail i";
        TypedQuery<Invoice_detail> invoice_detailTypedQuery = entityManager.createQuery(query, Invoice_detail.class);
        return invoice_detailTypedQuery.getResultList();
    }

    @Override
    public Invoice_detail findById(Long id) {
        return entityManager.find(Invoice_detail.class, id);
    }

    @Override
    public void save(Invoice_detail model) {
        if (model.getIdInvoice() != null && model.getIdProduct() != null) {
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
    public void update(Invoice_detail model) {

    }

    @Override
    public Invoice_detail findByName(String name) {
        return null;
    }
}
