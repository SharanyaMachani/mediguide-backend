
package com.example.demo.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(
            RuntimeException exception) {

        String message = exception.getMessage();

        if ("Invalid email or password".equals(message)
                || "Invalid role".equals(message)) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "Incorrect credentials. Please check your email, password, and role."
                    ));
        }

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "message",
                        message == null ? "Something went wrong." : message
                ));
    }
}