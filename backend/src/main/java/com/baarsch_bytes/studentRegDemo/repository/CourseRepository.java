package com.baarsch_bytes.studentRegDemo.repository;

import com.baarsch_bytes.studentRegDemo.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
