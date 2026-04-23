package com.baarsch_bytes.studentRegDemo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // This handler will take handle the validation exceptions.
    // It will create a Response entity and add the error messages from
    // the thrown exception to the response and send it back instead of
    // the exception.
    // This will handle all standard Springboot Validation Exceptions--
    // it is not tied to any specific class.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
            handleValidationExceptions(MethodArgumentNotValidException e) {

        // create the map that will hold all the errors and messages
        Map<String, String> errors = new HashMap<>();

        // get the errors from the exception and add them to the error map
        e.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        // return an HTTP Error Response with the errors and messages attached
        return ResponseEntity.badRequest().body(errors);
    }
}
