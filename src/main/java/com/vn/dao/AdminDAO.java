package com.vn.dao;

import com.vn.model.Admin;
import com.vn.service.AdminService;
import com.vn.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class AdminDAO implements AdminService {
    SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

    @Override
    @SuppressWarnings(value = { "unchecked" })
    public List<Admin> findAllAdmin() {
        Session session = null;
        Transaction transaction = null;
        List<Admin> list = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            list = session.createQuery("FROM Admin ").list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            assert session != null;
            session.close();
        }
        return list;
    }

    @Override
    public void save(Admin admin) {

    }

    @Override
    public Admin findByID(int id) {
        return null;
    }

    @Override
    public void updateAdmin(Admin admin) {

    }

    @Override
    public void removeAdmin(int id) {

    }
}
