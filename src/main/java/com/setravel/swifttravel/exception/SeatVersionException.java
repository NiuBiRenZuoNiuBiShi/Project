package com.setravel.swifttravel.exception;

public class SeatVersionException extends RuntimeException {
    public SeatVersionException(String message) {
        super(message);
    }

    public SeatVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeatVersionException(Throwable cause) {
        super(cause);
    }
}
