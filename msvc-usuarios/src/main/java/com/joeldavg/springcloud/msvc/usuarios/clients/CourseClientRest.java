package com.joeldavg.springcloud.msvc.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-courses", url = "${msvc.courses.url}")
//@FeignClient(name = "msvc-courses", url = "msvc-courses:8002/api/v1/courses")
public interface CourseClientRest {

    @DeleteMapping("remove-course-user/{userId}")
    void deleteCourseUserById(@PathVariable Long userId);

}
