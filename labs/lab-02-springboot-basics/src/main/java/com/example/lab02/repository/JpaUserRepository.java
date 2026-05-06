package com.example.lab02.repository;

import com.example.lab02.entity.JpaUserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<JpaUserEntity, Long> {

    Optional<JpaUserEntity> findByUsername(String username);

    Optional<JpaUserEntity> findByIdAndUsername(Long id, String username);
}
