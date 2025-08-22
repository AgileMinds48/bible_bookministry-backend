package com.evbooksministry.bibleandbookministry.exceptions;

public class InvalidEmail extends RuntimeException {
    public InvalidEmail(String message) {
        super(message);
    }

    public InvalidEmail() {
    }
}
