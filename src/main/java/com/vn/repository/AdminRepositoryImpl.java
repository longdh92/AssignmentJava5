package com.vn.repository;

import com.vn.model.Admin;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class AdminRepositoryImpl implements AdminRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Admin> findAll() {
        String query = "select a from Admin a";
        TypedQuery<Admin> adminTypedQuery = entityManager.createQuery(query, Admin.class);
        return adminTypedQuery.getResultList();
    }

    @Override
    public Admin findById(Long id) {
        return entityManager.find(Admin.class, id);
    }

    @Override
    public void save(Admin model) {
        if (model.getIdAdmin() != null) {
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
    public void update(Admin model) {

    }

    @Override
    public Admin findByName(String name) {
        String query = "select a from Admin a where a.usernameAdmin = :usernameAdmin";
        TypedQuery<Admin> adminTypedQuery = entityManager.createQuery(query, Admin.class);
        adminTypedQuery.setParameter("usernameAdmin", name);
        return adminTypedQuery.getResultList().get(0);
    }

    @Override
    public List<Admin> findByRole(boolean role) {
        String query = "select a from Admin a where a.role = :role";
        TypedQuery<Admin> adminTypedQuery = entityManager.createQuery(query, Admin.class);
        adminTypedQuery.setParameter("role", role);
        return adminTypedQuery.getResultList();
    }
}
