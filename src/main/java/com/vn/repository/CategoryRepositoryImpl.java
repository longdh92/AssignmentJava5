package com.vn.repository;

import com.vn.model.Category;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CategoryRepositoryImpl implements CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Category> findAll() {
        String query = "select c from Category c";
        TypedQuery<Category> categoryTypedQuery = entityManager.createQuery(query, Category.class);
        return categoryTypedQuery.getResultList();
    }

    @Override
    public Category findById(Long id) {
        return entityManager.find(Category.class, id);
    }

    @Override
    public void save(Category model) {
        if (model.getIdCategory() != null) {
            // Update
            entityManager.merge(model);
        } else {
            entityManager.persist(model);
        }
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void update(Long id, Category model) {

    }
}
