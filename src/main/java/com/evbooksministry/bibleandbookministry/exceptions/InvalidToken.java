package com.evbooksministry.bibleandbookministry.exceptions;

public class InvalidToken extends RuntimeException {
    public InvalidToken(String message) {
        super(message);
    }

    public InvalidToken() {
    }
}
