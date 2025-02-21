package com.projectWork.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectWork.exception.ResourceNotFoundException;
import com.projectWork.model.User;
import com.projectWork.model.User.Role;
import com.projectWork.repository.UserRepository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** 
 * Un servizio in un'applicazione software è una classe che incapsula e implementa la logica 
 * di business dell'applicazione. 
 * Questo significa che il servizio è responsabile di eseguire operazioni, elaborare dati e 
 * coordinare le interazioni tra i vari componenti (come i repository, API esterne, ecc.) 
 * senza preoccuparsi della gestione della presentazione o dell'interfaccia utente.
 * 
 * A cosa serve un servizio?
   - Isolamento della logica di business: Separare la logica di business 
   dal livello di presentazione (controller) rende il codice più organizzato, 
   modulare e manutenibile.
   - Riusabilità: La logica incapsulata in un servizio può essere riutilizzata in più punti 
   dell'applicazione.
   
   Che differenze c'è tra un service e un controller?
   La differenza principale è data dalla separazione delle responsabilità:
	Controller:
	- È responsabile di gestire le richieste HTTP e di formattare le risposte.
	- Converte i dati provenienti dalla richiesta (ad es. JSON) in oggetti, 
	e viceversa per la risposta.
	
	Il controller si occupa della comunicazione con il client, della gestione degli status HTTP 
	e dell'autenticazione/autorization a livello di interfaccia.

	Service:
	- Incapsula la logica di business e le operazioni CRUD.
	- Interagisce con il repository o altre fonti dati per eseguire le operazioni.
	- Permette di centralizzare il codice di business, facilitando il riutilizzo, 
	la manutenzione e il testing (ad esempio, testando il service in isolamento dai controller).
	
	In pratica, spostare i metodi CRUD in un service significa che il controller delega la logica 
	di business a componenti specializzati, mantenendo il codice più modulare. 
	Questo porta a un'applicazione più facilmente manutenibile, testabile e riutilizzabile.

 * */

/**
 * Servizio che si occupa della generazione e della gestione dei token di autenticazione.
 * I token vengono memorizzati in una mappa in memoria, associando ad ogni token un oggetto AuthUser.
 */
@Service
public class TokenService {

	 // Mappa per associare un token (String) all'oggetto AuthUser
    private Map<String, AuthUser> tokens = new ConcurrentHashMap<>();
    @Autowired
    private UserRepository userRepository;

    /**
     * Metodo che genera un token casuale (UUID) e lo associa ad un utente autenticato.
     *
     * @param username lo username dell'utente
     * @param role     il ruolo dell'utente (es. admin o user)
     * @return il token generato
     */
    public String generateToken(String username, Role role) {
    	 // Genera un token univoco usando UUID
        String token = UUID.randomUUID().toString();
      
        System.out.println("Token generato: " + token); //stringa di debug in console
		User user = userRepository.findByUsername(username) 
        		.orElseThrow(() -> new ResourceNotFoundException("Author not found"));
        
        
        	
        	if(user.getRole().equals(Role.ADMIN)) {
        	user.setToken(token);
        	userRepository.save(user);
        	}
        return token;
    }

    /**
     * Restituisce l'oggetto AuthUser associato al token.
     *
     * @param token il token di autenticazione
     * @return l'oggetto AuthUser, oppure null se il token non è valido
     */
    public AuthUser getAuthUser(String token) {
        return tokens.get(token);
    }

    /**
     * Rimuove un token dalla mappa, ad esempio durante il logout.
     *
     * @param token il token da rimuovere
     */
    public void removeToken(String token) {
        tokens.remove(token);
    }
}
