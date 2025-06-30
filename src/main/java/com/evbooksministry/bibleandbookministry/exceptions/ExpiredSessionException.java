package com.evbooksministry.bibleandbookministry.exceptions;

public class ExpiredSessionException extends RuntimeException {
    public ExpiredSessionException(String message) {
        super(message);
    }

    public ExpiredSessionException() {
    }
}
