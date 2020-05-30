package com.vn.utils;

import com.vn.model.Admin;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

public class HibernateUtils {
    private static Properties properties = new Properties();
    private static Configuration cfg = new Configuration();

    static {
        properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        properties.setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");
        properties.setProperty(Environment.USER, "root");
        properties.setProperty(Environment.PASS, "123456");
        properties.setProperty(Environment.URL, "jdbc:mysql://localhost:3306/AssignmentJava5");

        cfg.setProperties(properties);
        cfg.addAnnotatedClass(Admin.class);
    }

    // Utility method to return SessionFactory
    public static SessionFactory getSessionFactory() {
        return cfg.buildSessionFactory();
    }
}
