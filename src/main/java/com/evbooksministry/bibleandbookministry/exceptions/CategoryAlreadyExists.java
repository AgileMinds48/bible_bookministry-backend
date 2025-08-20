package com.evbooksministry.bibleandbookministry.exceptions;

public class CategoryAlreadyExists extends RuntimeException {
    public CategoryAlreadyExists(String message) {
        super(message);
    }

    public CategoryAlreadyExists() {
    }
}
