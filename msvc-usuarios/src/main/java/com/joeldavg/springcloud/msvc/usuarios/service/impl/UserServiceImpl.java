package com.joeldavg.springcloud.msvc.usuarios.service.impl;

import com.joeldavg.springcloud.msvc.usuarios.clients.CourseClientRest;
import com.joeldavg.springcloud.msvc.usuarios.models.entity.User;
import com.joeldavg.springcloud.msvc.usuarios.repository.UserRepository;
import com.joeldavg.springcloud.msvc.usuarios.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseClientRest courseClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
        courseClientRest.deleteCourseUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllById(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

}
