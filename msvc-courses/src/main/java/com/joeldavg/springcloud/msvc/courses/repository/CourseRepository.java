package com.joeldavg.springcloud.msvc.courses.repository;

import com.joeldavg.springcloud.msvc.courses.models.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Modifying
    @Query("DELETE FROM CourseUser cu WHERE cu.userId = ?1")
    void deleteCourseUserById(Long userId);

}
