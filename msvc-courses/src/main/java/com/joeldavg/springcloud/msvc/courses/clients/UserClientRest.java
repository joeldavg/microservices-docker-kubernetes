package com.joeldavg.springcloud.msvc.courses.clients;

import com.joeldavg.springcloud.msvc.courses.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-users", url = "msvc-users:8001/api/v1/users")
public interface UserClientRest {

    @GetMapping("{id}")
    User findUser(@PathVariable Long id);

    @PostMapping
    User saveUser(@RequestBody User user);

    @GetMapping("users-courses")
    List<User> findUserByIds(@RequestParam List<Long> ids);

}
