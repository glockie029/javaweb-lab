package com.example.lab01.repository;

import com.example.lab01.entity.JpaOrderEntity;
import java.util.List;
import javax.persistence.EntityManager;

public class JpaOrderRepository {

    public JpaOrderEntity findById(EntityManager entityManager, Long id) {
        return entityManager.find(JpaOrderEntity.class, id);
    }

    public JpaOrderEntity findByIdAndOwner(EntityManager entityManager, Long id, String owner) {
        List<JpaOrderEntity> orders = entityManager.createQuery(
                        "from JpaOrderEntity where id = :id and owner = :owner", JpaOrderEntity.class)
                .setParameter("id", id)
                .setParameter("owner", owner)
                .setMaxResults(1)
                .getResultList();
        return orders.isEmpty() ? null : orders.get(0);
    }

    public List<JpaOrderEntity> findByOwnerJpqlVulnerable(EntityManager entityManager, String owner) {
        String jpql = "from JpaOrderEntity where owner = '" + owner + "'";
        return entityManager.createQuery(jpql, JpaOrderEntity.class).getResultList();
    }

    public List<JpaOrderEntity> findByOwnerJpqlSafe(EntityManager entityManager, String owner) {
        return entityManager.createQuery(
                        "from JpaOrderEntity where owner = :owner", JpaOrderEntity.class)
                .setParameter("owner", owner)
                .getResultList();
    }

    public List<JpaOrderEntity> findByOwnerNativeVulnerable(EntityManager entityManager, String owner) {
        String sql = "select * from jpa_orders where owner = '" + owner + "'";
        return entityManager.createNativeQuery(sql, JpaOrderEntity.class).getResultList();
    }

    public List<JpaOrderEntity> findByOwnerNativeSafe(EntityManager entityManager, String owner) {
        return entityManager.createNativeQuery(
                        "select * from jpa_orders where owner = ?", JpaOrderEntity.class)
                .setParameter(1, owner)
                .getResultList();
    }

    public int updateStatusById(EntityManager entityManager, Long id, String status) {
        return entityManager.createQuery(
                        "update JpaOrderEntity set status = :status where id = :id")
                .setParameter("status", status)
                .setParameter("id", id)
                .executeUpdate();
    }

    public int updateStatusByIdAndOwner(EntityManager entityManager, Long id, String status, String owner) {
        return entityManager.createQuery(
                        "update JpaOrderEntity set status = :status where id = :id and owner = :owner")
                .setParameter("status", status)
                .setParameter("id", id)
                .setParameter("owner", owner)
                .executeUpdate();
    }
}
