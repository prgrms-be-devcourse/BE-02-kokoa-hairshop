package com.prgms.kokoahairshop.reservation1.controller;

import com.prgms.kokoahairshop.reservation1.exception.DuplicateReservationException;
import com.prgms.kokoahairshop.reservation1.exception.ReservationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.prgms.kokoahairshop.reservation1.controller")
public class ReservationControllerAdvice1 {

    @ExceptionHandler
    public ResponseEntity<String> handleValidationException (MethodArgumentNotValidException e) {
        log.info("validationException", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleDuplicateReservationException (
        DuplicateReservationException e) {
        log.info("DuplicateReservation", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleReservationNotFoundException (ReservationNotFoundException e) {
        log.error("NotFoundUrlException", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> exceptionHandler (Exception e) {
        log.error("Exception", e);
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
