package com.angelinux.citasapi.common.exception;

import com.angelinux.citasapi.appointment.domain.InvalidRequestResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InvalidRequestResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        InvalidRequestResponseDTO invalidRequestResponse = new InvalidRequestResponseDTO(
                LocalDateTime.now(),
                "Invalid request",
                errors
        );

        return new ResponseEntity<>(invalidRequestResponse, HttpStatus.BAD_REQUEST);
    }
}
