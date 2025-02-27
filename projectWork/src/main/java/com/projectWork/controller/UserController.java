package com.projectWork.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.projectWork.exception.ResourceNotFoundException;
import com.projectWork.model.Course;
import com.projectWork.model.Gym;
import com.projectWork.model.Session;
//import com.projectWork.auth.AuthUser;
import com.projectWork.model.User;
import com.projectWork.model.User.Role;
import com.projectWork.repository.CourseRepository;
import com.projectWork.repository.GymRepository;
import com.projectWork.repository.SessionRepository;
//import com.projectWork.auth.TokenService;
import com.projectWork.repository.UserRepository;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {}) // Disabilita richieste CORS da origini esterne
public class UserController
{

	@Autowired
	private UserRepository userRepository;
	List<User> users = new ArrayList<>();

	@Autowired
	private GymRepository gymRepository;

	@Autowired
	private CourseRepository courseRepository;
	List<Course> courses = new ArrayList<>();;

	@Autowired
	private SessionRepository sessionRepository;
	List<Session> sessions = new ArrayList<>();;

	/*
	 * @Autowired private TokenService tokenService;
	 */
	
	@GetMapping("/userDetails")
	public ResponseEntity<Object> userDetails(@RequestHeader("Authorization") String authorizationHeader) 
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
		return ResponseEntity.ok(user);
	}
	
	
	
	@GetMapping("/showUsers/{id}")
	public Optional<User> showUserById(@PathVariable Long id)
	{
		Optional<User> user = userRepository.findById(id);
		return user;
	}

	@PostMapping("/addUser")
	public ResponseEntity<Object> addUser(@RequestBody User user)
	{
		Map<String, String> result = new HashMap<String, String>();
		if (userRepository.findByEmail(user.getEmail()).isPresent())
		{
			result.put("errore", "L'email è già presente nel database.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
		/*if (user.getSecretCode() == 9999)
		{
			user.setRole(Role.COACH);
		} else
		{
			user.setRole(Role.USER);
		}*/
		User newUser = userRepository.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}

	@PutMapping("/addUserGym/{userId}/{gymId}")
	public ResponseEntity<Object> updateUserGym(@PathVariable Long userId, @PathVariable Long gymId)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Found"));
		Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new ResourceNotFoundException("Gym not Found"));
		user.setGym(gym);
		userRepository.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}

	@PostMapping("/addUserGym/{userId}/{gymId}")
	public ResponseEntity<Object> addUserGym(@PathVariable Long userId, @PathVariable Long gymId)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Found"));

		Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new ResourceNotFoundException("Gym not Found"));
		user.setGym(gym);
		userRepository.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}

	@PutMapping("/addUserCourse/{userId}/{courseId}")
	public ResponseEntity<Object> addUserCourse(@PathVariable Long userId, @PathVariable Long courseId)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Found"));

		Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not Found"));

		if (user.getRole() == Role.COACH && user.getSecretCode() == 9999)
		{
			users.add(user);
			courses.add(course);

			course.setUsers(users);
			user.setCourses(courses);

			courseRepository.save(course);
			userRepository.save(user);

			return ResponseEntity.status(HttpStatus.CREATED).body(course);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user);
	}

	@PostMapping("/addUserSession/{userId}/{sessionId}")
	public ResponseEntity<Object> addUserSession(@PathVariable Long userId, @PathVariable Long sessionId)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Found"));

		Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new ResourceNotFoundException("Session not Found"));
			
		List<Session> sessions = user.getSessions();
	    List<User> users = session.getUsers();
	    
		users.add(user);
		sessions.add(session);
		
		userRepository.save(user);
		sessionRepository.save(session);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}
	
	/*@PutMapping("/addUserSession/{userId}/{sessionId}")
	public ResponseEntity<Object> updateUserSession(@PathVariable Long userId, @PathVariable Long sessionId)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Found"));

		Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new ResourceNotFoundException("Session not Found"));
	    
		users.add(user);
		sessions.add(session);
		
		userRepository.save(user);
		sessionRepository.save(session);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}*/
	
	@GetMapping("/userRole")
	public ResponseEntity<Object> userRole(@RequestHeader("Authorization") String authorizationHeader) 
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
		Role role = user.getRole();
		return ResponseEntity.ok(Collections.singletonMap("role", role.name()));
	}

}