package com.evbooksministry.bibleandbookministry.exceptions;

public class BookNotFound extends RuntimeException {
    public BookNotFound(String message) {
        super(message);
    }

    public BookNotFound() {
    }
}
