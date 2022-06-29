package com.prgms.kokoahairshop.reservation2.exception;

public class ReservationNotReservedException extends RuntimeException {

    public ReservationNotReservedException() {
    }

    public ReservationNotReservedException(String message) {
        super(message);
    }
}
