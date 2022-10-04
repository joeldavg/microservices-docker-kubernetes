package com.joeldavg.springcloud.msvc.usuarios.service;

import com.joeldavg.springcloud.msvc.usuarios.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    void deleteById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAllById(List<Long> ids);

}
