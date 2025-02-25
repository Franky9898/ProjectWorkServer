package com.projectWork.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectWork.repository.GymRepository;
//import com.projectWork.auth.TokenService;
import com.projectWork.repository.UserRepository;
import com.projectWork.exception.ResourceNotFoundException;
import com.projectWork.model.Gym;
//import com.projectWork.auth.AuthUser;
import com.projectWork.model.User;
import com.projectWork.model.User.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {}) // Disabilita richieste CORS da origini esterne
public class UserController {

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private GymRepository gymRepository;

    /*@Autowired
    private TokenService tokenService;*/
    
    	@GetMapping("/showUsers/{id}")
    	public Optional <User> showUserById(@PathVariable Long id){	
    		Optional <User> user = userRepository.findById(id);
    		return user;
    	}
    	
    
		@PostMapping("/addUser")
		public ResponseEntity<Object> addUser(@RequestBody User user) {
			Map<String, String> result = new HashMap<String, String>();
			if(userRepository.findByEmail(user.getEmail()).isPresent()) {
				result.put("errore", "L'email è già presente nel database.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}
			if(user.getSecretCode() == 9999) {
				user.setRole(Role.COACH);
			} else {
				user.setRole(Role.USER);
			}
		    User newUser = userRepository.save(user);
		    return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
		}
		
		@PostMapping("/addUserGym/{userId}/{gymId}")
		public ResponseEntity<Object> addUserGym(@PathVariable Long userId, @PathVariable Long gymId ){
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new ResourceNotFoundException("User not Found"));
			
			Gym gym = gymRepository.findById(gymId)
					.orElseThrow(() -> new ResourceNotFoundException("Gym not Found"));
			user.setGym(gym);
			userRepository.save(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(user);
		}
		
		
		/*private AuthUser getAuthenticatedUser(HttpServletRequest request) {
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
		}*/
}