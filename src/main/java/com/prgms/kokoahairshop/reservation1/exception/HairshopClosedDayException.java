package com.prgms.kokoahairshop.reservation1.exception;

public class HairshopClosedDayException extends RuntimeException{

    public HairshopClosedDayException() {
    }

    public HairshopClosedDayException(String message) {
        super(message);
    }

    public HairshopClosedDayException(String message, Throwable cause) {
        super(message, cause);
    }

    public HairshopClosedDayException(Throwable cause) {
        super(cause);
    }

}
