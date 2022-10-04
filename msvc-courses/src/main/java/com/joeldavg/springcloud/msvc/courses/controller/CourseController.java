package com.joeldavg.springcloud.msvc.courses.controller;

import com.joeldavg.springcloud.msvc.courses.models.User;
import com.joeldavg.springcloud.msvc.courses.models.entity.Course;
import com.joeldavg.springcloud.msvc.courses.service.CourseService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "api/v1/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> findAll() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Course> findById(@PathVariable Long id) {
        Optional<Course> course = courseService.findByIdWithUsers(id);
        if (!course.isPresent()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(course.get());
    }

    @PostMapping
    public ResponseEntity<Course> save(
            @Valid
            @RequestBody
            Course course,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            validate(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.save(course));
    }

    @PutMapping("{id}")
    public ResponseEntity<Course> update(
            @Valid
            @RequestBody
            Course course,
            BindingResult result,
            @PathVariable Long id) {

        if (result.hasErrors()) {
            validate(result);
        }

        Optional<Course> courseDB = courseService.findById(id);

        if (!courseDB.isPresent()) return ResponseEntity.notFound().build();

        courseDB.ifPresent(c -> {
            c.setName(course.getName());
        });
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.save(courseDB.get()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        Optional<Course> course = courseService.findById(id);

        if (!course.isPresent()) return ResponseEntity.notFound().build();

        courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("add-user/{courseId}")
    public ResponseEntity addUser(@RequestBody User user, @PathVariable Long courseId) {
        Optional<User> userAdded;
        try {
            userAdded = courseService.addUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("error", "User doesnt exist " +
                            "or communication error: " + e.getMessage()));
        }
        if (userAdded.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userAdded.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("create-user/{courseId}")
    public ResponseEntity createUser(@RequestBody User user, @PathVariable Long courseId) {
        Optional<User> userCreated;
        try {
            userCreated = courseService.createUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("error", "User couldnt be created " +
                            "or communication error: " + e.getMessage()));
        }
        if (userCreated.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userCreated.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("remove-user/{courseId}")
    public ResponseEntity removeUser(@RequestBody User user, @PathVariable Long courseId) {
        Optional<User> userToDelete;
        try {
            userToDelete = courseService.removeUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("error", "User doesnt exist " +
                            "or communication error: " + e.getMessage()));
        }
        if (userToDelete.isPresent()) {
            return ResponseEntity.ok(userToDelete.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("remove-course-user/{userId}")
    public ResponseEntity deleteCourseUserById(@PathVariable Long userId) {
        courseService.deleteCourseUserById(userId);
        return ResponseEntity.noContent().build();
    }


    private static ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}