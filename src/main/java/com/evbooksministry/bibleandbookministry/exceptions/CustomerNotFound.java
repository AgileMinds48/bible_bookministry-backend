package com.evbooksministry.bibleandbookministry.exceptions;

public class CustomerNotFound extends RuntimeException {
    public CustomerNotFound(String message) {
        super(message);
    }

    public CustomerNotFound() {
    }
}
