package com.evbooksministry.bibleandbookministry.exceptions;

public class InsufficientBooks extends RuntimeException {
    public InsufficientBooks(String message) {
        super(message);
    }

    public InsufficientBooks() {
    }
}
