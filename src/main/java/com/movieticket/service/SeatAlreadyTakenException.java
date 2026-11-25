package com.movieticket.service;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class SeatAlreadyTakenException extends RuntimeException {

    public SeatAlreadyTakenException(String message) {
        super(message);
    }
}
