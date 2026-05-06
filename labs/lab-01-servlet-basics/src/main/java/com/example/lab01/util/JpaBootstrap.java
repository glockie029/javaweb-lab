package com.example.lab01.util;

import com.example.lab01.entity.JpaOrderEntity;
import com.example.lab01.entity.JpaUserEntity;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class JpaBootstrap {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = buildEntityManagerFactory();

    private JpaBootstrap() {
    }

    public static EntityManager openEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    private static EntityManagerFactory buildEntityManagerFactory() {
        try {
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("javax.persistence.jdbc.driver", "org.h2.Driver");
            properties.put("javax.persistence.jdbc.url", "jdbc:h2:mem:lab01jpademo;MODE=MySQL;DB_CLOSE_DELAY=-1");
            properties.put("javax.persistence.jdbc.user", "sa");
            properties.put("javax.persistence.jdbc.password", "");
            properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
            properties.put("hibernate.hbm2ddl.auto", "create");
            properties.put("hibernate.show_sql", "false");
            properties.put("hibernate.format_sql", "false");

            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
                    "lab01-jpa-unit", properties);
            initializeDatabase(entityManagerFactory);
            return entityManagerFactory;
        } catch (Exception ex) {
            throw new IllegalStateException("初始化 JPA/Hibernate 失败", ex);
        }
    }

    private static void initializeDatabase(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(new JpaOrderEntity(1001L, "alice", "JPA Security Notes", "CREATED"));
            entityManager.persist(new JpaOrderEntity(1002L, "alice", "Hibernate Cookbook", "PAID"));
            entityManager.persist(new JpaOrderEntity(2001L, "bob", "Spring Audit Practice", "CREATED"));
            entityManager.persist(new JpaOrderEntity(2002L, "bob", "ORM Pitfall Report", "SHIPPED"));

            entityManager.persist(new JpaUserEntity(
                    1L, "alice", "alice@example.com", "user", "hash-alice-123"));
            entityManager.persist(new JpaUserEntity(
                    2L, "bob", "bob@example.com", "user", "hash-bob-456"));
            entityManager.persist(new JpaUserEntity(
                    3L, "admin", "admin@example.com", "admin", "hash-admin-789"));
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new IllegalStateException("初始化 JPA 演示数据失败", ex);
        } finally {
            entityManager.close();
        }
    }
}
