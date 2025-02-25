package com.projectWork.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectWork.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>
{
	Optional<Course> findByTitle(String title);
}
