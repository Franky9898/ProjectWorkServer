package com.projectWork.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectWork.repository.CourseRepository;
import com.projectWork.repository.UserRepository;
import com.projectWork.model.Course;
import com.projectWork.model.User;
import com.projectWork.model.User.Role;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = {}) // Disabilita richieste CORS da origini esterne
public class CoachController
{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CourseRepository courseRepository;

	@GetMapping("/showCourses")
	public List <Course> showAllCourses()
	{	
		return courseRepository.findAll();
	}
	
	@GetMapping("/showCoursesById")
	public Optional<Course> showCourseById(@PathVariable Long id)
	{
		Optional<Course> course = courseRepository.findById(id);
		return course;
	}

	@PostMapping("/addCourse")
	public ResponseEntity<String> addCourse(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Course course)
	{
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Errore: Header Authorization mancante o formato errato.");
		}

		String token = authorizationHeader.substring(7);
		Optional<User> userOpt = userRepository.findByToken(token);
		if (!userOpt.isPresent())
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato.");
		}
		User user = userOpt.get();
		if (!(user.getRole() == Role.COACH && user.getSecretCode() == 9999))
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non autorizzato.");
		}
		List<User> courseUsers = course.getUsers();
		if (courseUsers == null)
		{
			courseUsers = new ArrayList<>();
		}
		if (!courseUsers.contains(user))
		{
			courseUsers.add(user);
		}
		course.setUsers(courseUsers);
		List<Course> userCourses = user.getCourses();
		if (userCourses == null)
		{
			userCourses = new ArrayList<>();
		}
		if (!userCourses.contains(course))
		{
			userCourses.add(course);
		}
		user.setCourses(userCourses);
		courseRepository.save(course);
		userRepository.save(user);
		return ResponseEntity.ok("Corso aggiunto con successo.");
	}

	@PutMapping("/addCoachToCourse")
	public ResponseEntity<String> addCoachToCourse(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> body)
	{
		String result;
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
		{
			result = "Errore: Header Authorization mancante o formato errato.";
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
		}
		String token = authorizationHeader.substring(7);
		Optional<User> userOpt = userRepository.findByToken(token);
		if (!userOpt.isPresent())
		{
			result = "User not found";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		}

		String newCoachEmail = body.get("email");
		Optional<User> newCoachOpt = userRepository.findByEmail(newCoachEmail);
		if (!newCoachOpt.isPresent())
		{
			result = "Errore: Nessun Utente con questa email.";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		}
		User newCoach = newCoachOpt.get();
		if (newCoach.getRole() != Role.COACH || newCoach.getSecretCode() != 9999)
		{
			result = "Errore: Nessun Coach con questa email.";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		}
		User user = userOpt.get();
		if (user.getRole() != Role.COACH || user.getSecretCode() != 9999)
		{
			result = "Errore: Non dovresti essere qui.";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		}
		String courseTitle = body.get("title");
		Optional<Course> courseOpt = courseRepository.findByTitle(courseTitle);
		if (!courseOpt.isPresent())
		{
			result = "Errore: Nessun Corso con questo titolo.";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		}
		Course course = courseOpt.get();
		List<User> courseUsers = course.getUsers();
		if (courseUsers == null)
		{
			courseUsers = new ArrayList<>();
		}
		if (!courseUsers.contains(user))
		{
			courseUsers.add(user);
		}
		if (!courseUsers.contains(newCoach))
		{
			courseUsers.add(newCoach);
		}
		course.setUsers(courseUsers);
		courseRepository.save(course);

		List<Course> userCourses = user.getCourses();
		if (userCourses == null)
		{
			userCourses = new ArrayList<>();
		}
		if (!userCourses.contains(course))
		{
			userCourses.add(course);
		}
		user.setCourses(userCourses);

		List<Course> coachCourses = newCoach.getCourses();
		if (coachCourses == null)
		{
			coachCourses = new ArrayList<>();
		}
		if (!coachCourses.contains(course))
		{
			coachCourses.add(course);
		}
		newCoach.setCourses(coachCourses);
		userRepository.saveAll(Arrays.asList(user, newCoach));
		result = "Coach aggiunto con successo al corso selezionato";
		return ResponseEntity.ok(result);
	}

}
