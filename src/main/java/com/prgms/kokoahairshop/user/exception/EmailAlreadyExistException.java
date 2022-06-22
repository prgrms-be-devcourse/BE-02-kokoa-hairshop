package com.prgms.kokoahairshop.user.exception;

public class EmailAlreadyExistException extends RuntimeException{

    public EmailAlreadyExistException() {
    }

    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
