package com.projectWork.exception; // Package dedicato alla gestione delle eccezioni

import org.springframework.http.HttpStatus; // Importa gli status HTTP (ad es. 404, 500)
import org.springframework.http.ResponseEntity; // Importa la classe per costruire le risposte HTTP
import org.springframework.web.bind.annotation.ControllerAdvice; // Importa l'annotazione per la gestione globale delle eccezioni
import org.springframework.web.bind.annotation.ExceptionHandler; // Importa l'annotazione per specificare i metodi gestori di eccezioni
import org.springframework.web.context.request.WebRequest; // Importa l'interfaccia per accedere ai dettagli della richiesta

import java.util.Date; // Importa la classe Date per gestire i timestamp

@ControllerAdvice // Indica a Spring che questa classe fornisce gestione centralizzata delle eccezioni per tutti i controller
public class GlobalExceptionHandler {

    // Gestisce le eccezioni di tipo ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class) // Specifica che questo metodo si attiva quando viene lanciata una ResourceNotFoundException
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        // Crea un oggetto ErrorDetails con la data corrente, il messaggio dell'eccezione e una descrizione della richiesta
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        // Restituisce una ResponseEntity contenente l'oggetto ErrorDetails e uno status HTTP 404 NOT FOUND
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // Gestisce tutte le altre eccezioni che non sono specificatamente gestite altrove
    @ExceptionHandler(Exception.class) // Specifica che questo metodo si attiva per ogni eccezione di tipo Exception
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        // Crea un oggetto ErrorDetails con la data corrente, il messaggio dell'eccezione e una descrizione della richiesta
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        // Restituisce una ResponseEntity contenente l'oggetto ErrorDetails e uno status HTTP 500 INTERNAL SERVER ERROR
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Classe interna per strutturare i dettagli dell'errore da restituire al client
    public static class ErrorDetails {
        private Date timestamp; // Data e ora in cui si è verificato l'errore
        private String message; // Messaggio descrittivo dell'errore
        private String details; // Dettagli aggiuntivi relativi all'errore, ad esempio informazioni sulla richiesta

        // Costruttore per inizializzare tutti i campi di ErrorDetails
        public ErrorDetails(Date timestamp, String message, String details) {
            super(); // Chiamata al costruttore della superclasse (non strettamente necessaria in questo caso)
            this.timestamp = timestamp; // Inizializza il campo timestamp
            this.message = message;     // Inizializza il campo message
            this.details = details;     // Inizializza il campo details
        }

        // Getter per il timestamp
        public Date getTimestamp() {
            return timestamp;
        }

        // Getter per il messaggio
        public String getMessage() {
            return message;
        }

        // Getter per i dettagli
        public String getDetails() {
            return details;
        }
    }
}
