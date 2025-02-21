package com.projectWork.exception; // Package in cui risiede l'eccezione custom per le risorse non trovate

import org.springframework.http.HttpStatus; // Importa HttpStatus per definire i codici di stato HTTP
import org.springframework.web.bind.annotation.ResponseStatus; // Importa l'annotazione per associare uno status HTTP all'eccezione

// L'annotazione @ResponseStatus indica che, se questa eccezione viene lanciata, il server restituisce lo status HTTP 404 NOT FOUND
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // Costruttore che accetta un messaggio descrittivo dell'errore
    public ResourceNotFoundException(String message) {
        // Chiama il costruttore della superclasse RuntimeException passando il messaggio d'errore
        super(message);
    }
}
