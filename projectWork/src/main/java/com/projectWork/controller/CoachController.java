package com.projectWork.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectWork.repository.CourseRepository;
import com.projectWork.repository.GymRepository;
import com.projectWork.repository.SessionRepository;
//import com.projectWork.auth.TokenService;
import com.projectWork.repository.UserRepository;
import com.projectWork.exception.ResourceNotFoundException;
import com.projectWork.model.Course;
import com.projectWork.model.Gym;
import com.projectWork.model.Session;
//import com.projectWork.auth.AuthUser;
import com.projectWork.model.User;
import com.projectWork.model.User.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = {}) // Disabilita richieste CORS da origini esterne
public class CoachController {

	@Autowired
    private UserRepository userRepository;
	List <User> users = new ArrayList<>();;
	
	@Autowired
    private GymRepository gymRepository;
	
	@Autowired
    private CourseRepository courseRepository;
	List <Course> courses = new ArrayList<>();
	
	@Autowired
    private SessionRepository sessionRepository;
	List <Session> sessions = new ArrayList<>();
	
	@GetMapping("/showCourses")
	public List <Course> showAllCourses(){	
		return courseRepository.findAll();
	}
	
	@PostMapping("/addCourse/{userId}")
	public ResponseEntity<Object> addCourse(@PathVariable Long userId, @RequestBody Course course) {
		Optional <User> userOpt = userRepository.findById(userId);
		if(!userOpt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
		User user = userOpt.get();
		if(user.getRole() == Role.COACH && user.getSecretCode() == 9999) {
			users.add(user);
			courses.add(course);
			
			user.setCourses(courses);
			course.setUsers(users);
			
			Course newCourse = courseRepository.save(course);
			userRepository.save(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
		}
	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user);
	}
	
	@PutMapping("/addCoachToCourse/{coachId}/{courseId}")
	public ResponseEntity<Object> addCoachToCourse(@PathVariable Long coachId, @PathVariable Long courseId){
		Optional <User> coachOpt = userRepository.findById(coachId);
		Optional <Course> courseOpt = courseRepository.findById(courseId);
		if(!coachOpt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
		User coach = coachOpt.get();
		Course course = courseOpt.get();
		if(coach.getRole() == Role.COACH && coach.getSecretCode() == 9999) {
			users.add(coach);
			courses.add(course);
			
			coach.setCourses(courses);
			course.setUsers(users);
			
			courseRepository.save(course);
			userRepository.save(coach);
			return ResponseEntity.status(HttpStatus.CREATED).body(course);
		}
	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(coach);
	}
	
}

/* 
 * {
  "firstName": "Lorenzo",
  "lastName": "Frascella",
  "email": "lore12345@gmail.com",
  "password": "lollo",
  "role": 1,
  "secretCode": 9999
}

http://localhost:8080/courses/addCoachToCourse/5/6

{
  "id": 3,
  "title": "boh",
  "description": "bohbohboh"
}

 * */
