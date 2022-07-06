package com.prgms.kokoahairshop.reservation.exception;

public class ReservationCancelTimeoutException extends RuntimeException {

    public ReservationCancelTimeoutException() {
    }

    public ReservationCancelTimeoutException(String message) {
        super(message);
    }
}
