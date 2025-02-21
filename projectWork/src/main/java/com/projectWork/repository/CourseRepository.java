package com.projectWork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectWork.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long>
{

}
