package com.joeldavg.springcloud.msvc.usuarios.repository;

import com.joeldavg.springcloud.msvc.usuarios.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> getByEmail(String email);

    boolean existsByEmail(String email);

}