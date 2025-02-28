package com.projectWork.controller;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectWork.model.Course;
import com.projectWork.model.Session;
import com.projectWork.model.User;
import com.projectWork.repository.CourseRepository;
import com.projectWork.repository.SessionRepository;
import com.projectWork.repository.UserRepository;
import com.projectWork.service.SessionService;

@RestController
@RequestMapping("/sessions")
@CrossOrigin(origins = {})
public class SessionController
{
	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CourseRepository courseRepository;

	@GetMapping("/showSessions")
	public List<Map<String, Object>> showAllSessions()
	{
		List<Session> sessions = sessionRepository.findAll();
		List<Map<String, Object>> sessionsList = new ArrayList<>();
		for(Session session : sessions) 
		{
			Map<String, Object> sessionMap = new HashMap<>();
			sessionMap.put("startingTime", session.getStartingTime());
			sessionMap.put("title", session.getCourse().getTitle());
			sessionMap.put("id", session.getId());
			sessionMap.put("description", session.getCourse().getDescription());
			sessionsList.add(sessionMap);
		}
		return sessionsList;
	}

	@GetMapping("/showUserSessions")
	public List<Session> showAllUserSessions(@RequestHeader("Authorization") String authorizationHeader)
	{
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
		{
			return null;
		}

		String token = authorizationHeader.substring(7);
		Optional<User> userOpt = userRepository.findByToken(token);
		if (!userOpt.isPresent())
		{
			return null;
		}

		User user = userOpt.get();
		List<Session> userSessions = sessionRepository.findByUsersContaining(user);

		return userSessions;
	}

	@GetMapping("/showSessionsByCourseId/{id}")
	public List<Session> showSessionsByCourseId(@PathVariable Long id)
	{
		Optional<Course> courseOpt = courseRepository.findById(id);
		Course course = courseOpt.get();
		List<Session> sessions = course.getSessions();
		return sessions;
	}

	@PostMapping
	public ResponseEntity<String> coachCreateSession(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Session session)
	{
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Errore: Header Authorization mancante o formato errato.");
		}
		String token = authorizationHeader.substring(7);
		System.out.println("Token estratto: " + token);
		Optional<User> coachOpt = userRepository.findByToken(token);
		if (!coachOpt.isPresent() || coachOpt.get().getRole() != User.Role.COACH)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Errore: il token non corrisponde a nessun istruttore.");
		}
		return sessionService.createSession(session);
	}

}
