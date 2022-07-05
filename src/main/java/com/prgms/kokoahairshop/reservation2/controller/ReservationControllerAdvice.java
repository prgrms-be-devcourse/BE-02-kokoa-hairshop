package com.prgms.kokoahairshop.reservation2.controller;

import com.prgms.kokoahairshop.common.exception.NotFoundException;
import com.prgms.kokoahairshop.reservation2.exception.DuplicateReservationException;
import com.prgms.kokoahairshop.reservation2.exception.ReservationCancelTimeoutException;
import com.prgms.kokoahairshop.reservation2.exception.ReservationNotReservedException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.prgms.kokoahairshop.reservation2.controller")
public class ReservationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(
        MethodArgumentNotValidException e) {
        List<String> errorMassages = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(c -> errorMassages.add(c.getDefaultMessage()));

        return ResponseEntity.badRequest()
            .body(errorMassages);
    }

    @ExceptionHandler(DuplicateReservationException.class)
    ResponseEntity<String> handleDuplicateReservationException(DuplicateReservationException e) {
        return ResponseEntity.badRequest()
            .body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.notFound()
            .build();
    }

    @ExceptionHandler(ReservationNotReservedException.class)
    ResponseEntity<String> handleReservationNotReservedException(
        ReservationNotReservedException e) {
        return ResponseEntity.badRequest()
            .body(e.getMessage());
    }

    @ExceptionHandler(ReservationCancelTimeoutException.class)
    ResponseEntity<String> handleReservationCancelTimeoutException(
        ReservationCancelTimeoutException e) {
        return ResponseEntity.badRequest()
            .body(e.getMessage());
    }
}