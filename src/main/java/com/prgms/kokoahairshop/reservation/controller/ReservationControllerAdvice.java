package com.prgms.kokoahairshop.reservation.controller;

import com.prgms.kokoahairshop.common.exception.NotFoundException;
import com.prgms.kokoahairshop.reservation.exception.DuplicateReservationException;
import com.prgms.kokoahairshop.reservation.exception.ReservationCancelTimeoutException;
import com.prgms.kokoahairshop.reservation.exception.ReservationNotReservedException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.prgms.kokoahairshop.reservation.controller")
public class ReservationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(
        MethodArgumentNotValidException e) {
        log.info("Validation Exception", e);
        List<String> errorMessages = new ArrayList<>();
        e.getBindingResult().getAllErrors()
            .forEach(c -> errorMessages.add(c.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errorMessages);
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<String> handleDuplicateReservationException(
        DuplicateReservationException e) {
        log.info("Duplicate Reservation", e);

        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<Void> handleNotFoundException(NotFoundException e) {
        log.info("Reservation Not Found", e);

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ReservationNotReservedException.class)
    ResponseEntity<String> handleReservationNotReservedException(
        ReservationNotReservedException e) {
        log.error("Reservation Not Reserved Exception", e);

        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ReservationCancelTimeoutException.class)
    ResponseEntity<String> handleReservationCancelTimeoutException(
        ReservationCancelTimeoutException e) {
        log.error("Reservation Cancel Timeout Exception", e);

        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgumentException(
        IllegalArgumentException e) {
        log.error("Identification error", e);

        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Exception", e);

        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}