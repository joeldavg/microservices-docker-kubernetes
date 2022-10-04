package com.joeldavg.springcloud.msvc.courses.service;

import com.joeldavg.springcloud.msvc.courses.models.User;
import com.joeldavg.springcloud.msvc.courses.models.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> findAll();

    Optional<Course> findById(Long id);

    Optional<Course> findByIdWithUsers(Long id);

    Course save(Course course);

    void deleteById(Long id);

    void deleteCourseUserById(Long userId);

    Optional<User> addUser(User user, Long courseId);

    Optional<User> createUser(User user, Long courseId);

    Optional<User> removeUser(User user, Long courseId);

}
