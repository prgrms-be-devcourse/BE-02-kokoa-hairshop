package com.prgms.kokoahairshop.reservation1.exception;

public class DuplicateReservationException extends RuntimeException {

    public DuplicateReservationException() {
    }

    public DuplicateReservationException(String message) {
        super(message);
    }

    public DuplicateReservationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateReservationException(Throwable cause) {
        super(cause);
    }
}
