package com.joeldavg.springcloud.msvc.usuarios.controller;

import com.joeldavg.springcloud.msvc.usuarios.models.entity.User;
import com.joeldavg.springcloud.msvc.usuarios.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (!user.isPresent()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user.get());
    }

    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity save(
            @Valid
            @RequestBody
            User user,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return validate(result);
        }

        if (userService.existsByEmail(user.getEmail())) return ResponseEntity.badRequest()
                .body(Collections.singletonMap("error", "email already exists"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.save(user));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(
            @Valid
            @RequestBody
            User user,
            BindingResult result,
            @PathVariable Long id
    ) {

        if (result.hasErrors()) {
            return validate(result);
        }

        Optional<User> userById = userService.findById(id);
        if (userById.isPresent()) {
            User userDB = userById.get();
            if (user.getEmail().equalsIgnoreCase(userDB.getEmail()) || userService.existsByEmail(user.getEmail())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "email already exists"));
            }
            userDB.setName(user.getName());
            userDB.setEmail(user.getEmail());
            userDB.setPassword(user.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userService.save(userDB));
        }

        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);

        if (!user.isPresent()) return ResponseEntity.notFound().build();

        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("users-courses")
    public ResponseEntity<List<User>> findUsersByCourse(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(userService.findAllById(ids));
    }

    private static ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + ", " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
