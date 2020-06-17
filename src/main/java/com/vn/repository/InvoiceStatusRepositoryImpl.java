package com.vn.repository;

import com.vn.model.Invoice;
import com.vn.model.InvoiceStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
public class InvoiceStatusRepositoryImpl implements InvoiceStatusRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<InvoiceStatus> findAll() {
        String query = "select i from InvoiceStatus i";
        TypedQuery<InvoiceStatus> invoiceStatusTypedQuery = entityManager.createQuery(query, InvoiceStatus.class);
        return invoiceStatusTypedQuery.getResultList();
    }

    @Override
    public InvoiceStatus findById(Long id) {
        return entityManager.find(InvoiceStatus.class, id);
    }

    @Override
    public void save(InvoiceStatus model) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void update(InvoiceStatus model) {

    }

    @Override
    public InvoiceStatus findByName(String name) {
        String query = "select i from InvoiceStatus i where i.statusName = :statusName";
        TypedQuery<InvoiceStatus> invoiceStatusTypedQuery = entityManager.createQuery(query, InvoiceStatus.class);
        invoiceStatusTypedQuery.setParameter("statusName", name);
        if (invoiceStatusTypedQuery.getResultList().size() == 0) {
            return null;
        }
        return invoiceStatusTypedQuery.getResultList().get(0);
    }
}
