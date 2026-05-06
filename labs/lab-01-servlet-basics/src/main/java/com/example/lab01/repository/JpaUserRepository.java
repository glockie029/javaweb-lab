package com.example.lab01.repository;

import com.example.lab01.entity.JpaUserEntity;
import javax.persistence.EntityManager;

public class JpaUserRepository {

    public JpaUserEntity findById(EntityManager entityManager, Long id) {
        return entityManager.find(JpaUserEntity.class, id);
    }

    public JpaUserEntity save(EntityManager entityManager, JpaUserEntity user) {
        return entityManager.merge(user);
    }
}
