package com.projectWork.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectWork.model.Course;
import com.projectWork.model.Session;
import com.projectWork.model.User;
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
	
	@GetMapping("/showSessions")
	public List <Session> showAllSessions(){
		return sessionRepository.findAll();
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
