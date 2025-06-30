package com.evbooksministry.bibleandbookministry.exceptions;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String message) {
        super(message);
    }

    public UserAlreadyExists() {
    }
}
