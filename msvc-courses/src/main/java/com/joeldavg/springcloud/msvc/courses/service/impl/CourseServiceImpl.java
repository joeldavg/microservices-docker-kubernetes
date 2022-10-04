package com.joeldavg.springcloud.msvc.courses.service.impl;

import com.joeldavg.springcloud.msvc.courses.clients.UserClientRest;
import com.joeldavg.springcloud.msvc.courses.models.User;
import com.joeldavg.springcloud.msvc.courses.models.entity.Course;
import com.joeldavg.springcloud.msvc.courses.models.entity.CourseUser;
import com.joeldavg.springcloud.msvc.courses.repository.CourseRepository;
import com.joeldavg.springcloud.msvc.courses.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserClientRest userClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findByIdWithUsers(Long id) {
        Optional<Course> c = courseRepository.findById(id);
        Course course = null;
        if (c.isPresent()) {
            course = c.get();
            List<Long> ids = course.getCourseUsers().stream()
                    .map(CourseUser::getUserId)
                    .collect(Collectors.toList());
            List<User> users = userClientRest.findUserByIds(ids);
            course.setUsers(users);
        }
        return Optional.ofNullable(course);
    }

    @Override
    @Transactional
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCourseUserById(Long userId) {
        courseRepository.deleteCourseUserById(userId);
    }

    @Override
    @Transactional
    public Optional<User> addUser(User user, Long courseId) {
        Optional<Course> courseDB = courseRepository.findById(courseId);
        User userMsvc = null;
        if (courseDB.isPresent()) {

            userMsvc = userClientRest.findUser(user.getId());

            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());

            Course course = courseDB.get();
            course.addCourseUser(courseUser);
            courseRepository.save(course);
        }
        return Optional.ofNullable(userMsvc);
    }

    @Override
    @Transactional
    public Optional<User> createUser(User user, Long courseId) {
        Optional<Course> courseDB = courseRepository.findById(courseId);
        User userMsvc = null;
        if (courseDB.isPresent()) {

            userMsvc = userClientRest.saveUser(user);

            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());

            Course course = courseDB.get();
            course.addCourseUser(courseUser);
            courseRepository.save(course);
        }
        return Optional.ofNullable(userMsvc);
    }

    @Override
    @Transactional
    public Optional<User> removeUser(User user, Long courseId) {
        Optional<Course> courseDB = courseRepository.findById(courseId);
        User userMsvc = null;
        if (courseDB.isPresent()) {

            userMsvc = userClientRest.findUser(user.getId());

            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());

            Course course = courseDB.get();
            course.removeCourseUser(courseUser);
            courseRepository.save(course);
        }
        return Optional.ofNullable(userMsvc);
    }

}
