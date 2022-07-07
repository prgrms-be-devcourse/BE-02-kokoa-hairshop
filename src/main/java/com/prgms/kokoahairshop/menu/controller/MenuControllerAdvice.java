package com.prgms.kokoahairshop.menu.controller;

import com.prgms.kokoahairshop.common.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.prgms.kokoahairshop.menu.controller")
public class MenuControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
        log.info("validationException", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        log.info("notFoundException", e);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> exceptionHandler(Exception e) {
        log.error("Exception", e);
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
