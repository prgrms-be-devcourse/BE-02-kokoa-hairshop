package com.prgms.kokoahairshop.reservation2.controller;

import com.prgms.kokoahairshop.reservation2.exception.DuplicateReservationException;
import com.prgms.kokoahairshop.reservation2.exception.ReservationCancelTimeoutException;
import com.prgms.kokoahairshop.reservation2.exception.ReservationNotFoundException;
import com.prgms.kokoahairshop.reservation2.exception.ReservationNotReservedException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.prgms.kokoahairshop.reservation.controller")
public class ReservationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(
        MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors()
            .forEach(c -> errors.add(c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DuplicateReservationException.class)
    ResponseEntity<String> handleDuplicateReservationException(DuplicateReservationException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(e.getMessage());
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    ResponseEntity<Object> handleReservationNotFoundException(ReservationNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ReservationNotReservedException.class)
    ResponseEntity<String> handleReservationNotReservedException(
        ReservationNotReservedException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ReservationCancelTimeoutException.class)
    ResponseEntity<String> handleReservationCancelTimeoutException(
        ReservationCancelTimeoutException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}