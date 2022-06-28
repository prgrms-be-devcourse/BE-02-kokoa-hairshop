package com.prgms.kokoahairshop.reservation2.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException() {
    }

    public ReservationNotFoundException(String message) {
        super(message);
    }
}
