package com.projectWork.controller;

import com.projectWork.auth.TokenService;
import com.projectWork.model.User;
import com.projectWork.model.User.Role;
import com.projectWork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controller per gestire l'autenticazione e il login.
 * Riceve username e password in formato JSON, verifica le credenziali nel database e, in caso di successo,
 * restituisce un token nel body della risposta.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {}) // Disabilita richieste CORS da origini esterne
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint per effettuare il login.
     * Riceve username e password in formato JSON e restituisce il token se le credenziali sono valide.
     *
     * @param body     mappa contenente "username" e "password"
     * @param response oggetto HttpServletResponse per impostare lo status
     * @return mappa con un messaggio di conferma, il ruolo dell'utente e il token
     */
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body, HttpServletResponse response) {
        
    	 // Estrae username e password dalla richiesta JSON
    	String email = body.get("email");
        String password = body.get("password");

        // Mappa per la risposta
        Map<String, String> result = new HashMap<>();

        // Verifica che username e password siano stati forniti
        if (email == null || password == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            result.put("message", "Credenziali non valide");
            return result;
        }

        // Cerca l'utente nel database tramite username
        Optional<User> optionalUser = userRepository.findByEmail(email);
        // Se l'utente non esiste o la password non corrisponde, ritorna errore 401
        if (!optionalUser.isPresent() || !optionalUser.get().getPassword().equals(password)) {
            // Se l'utente non esiste oppure la password non corrisponde, restituisce 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            result.put("message", "Credenziali non valide");
            return result;
        }

        // Se le credenziali sono corrette, recupera l'utente e il suo ruolo
        User user = optionalUser.get();
        Role role = user.getRole();

        // Genera un token associato all'utente
        String token = tokenService.generateToken(email, role);

        // Costruisce la risposta con messaggio, ruolo e token
        result.put("message", "Login effettuato con successo");
        result.put("role", role.toString());
        result.put("token", token);
        return result;
    }
    

    /**
     * Endpoint per effettuare il logout.
     * Riceve il token nell'header "Authorization" e lo rimuove dal TokenService, 
     * invalidando così il token lato client.
     *
     * @param authHeader header contenente il token (formato "Bearer <token>")
     * @return mappa con un messaggio di conferma del logout
     */
    @PostMapping("/logout")
    public Map<String, String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = null;
        // Se il token è inviato come "Bearer <token>", estrae la parte dopo "Bearer " e quindi il token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            token = authHeader;
        }
        // Rimuove il token dal TokenService
        tokenService.removeToken(token);

        Map<String, String> result = new HashMap<>();
        result.put("message", "Logout effettuato con successo");
        return result;
    }
}

