package com.projectWork.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectWork.auth.TokenService;
import com.projectWork.repository.UserRepository;
import com.projectWork.auth.AuthUser;
import com.projectWork.model.User;
import com.projectWork.model.User.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {}) // Disabilita richieste CORS da origini esterne
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;
    
		@PostMapping("/addUser")
		public Object addUser(@RequestBody User newUser, HttpServletRequest request, HttpServletResponse response) {
			// Verifica l'autenticazione
			AuthUser authUser = getAuthenticatedUser(request);
		    if (authUser == null) {
		        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        return Collections.singletonMap("message", "Non autorizzato");
		    }
		    // Verifica che l'utente abbia il ruolo "admin"
		    if (!"admin".equals(authUser.getRole())) {
		        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		        return Collections.singletonMap("message", "Accesso negato: solo admin può aggiungere utenti");
		    }
		    // Salva il nuovo utente nel database
		    newUser.setRole(Role.USER);
		    userRepository.save(newUser);
		    return Collections.singletonMap("message", "Utente aggiunto con successo");
		}
		
		private AuthUser getAuthenticatedUser(HttpServletRequest request) {
		    // Legge l'header "Authorization"
		    String authHeader = request.getHeader("Authorization");
		    if (authHeader != null && !authHeader.isEmpty()) {
		        String token;
		        // Se il token è inviato come "Bearer <token>", lo estrae
		        if (authHeader.startsWith("Bearer ")) {
		            token = authHeader.substring(7);
		        } else {
		            token = authHeader;
		        }
		        // Usa il TokenService per ottenere l'utente associato al token
		        return tokenService.getAuthUser(token);
		    }
		    // Se non c'è header "Authorization", restituisce null
		    return null;
		}
}