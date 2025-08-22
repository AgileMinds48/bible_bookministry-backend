package com.evbooksministry.bibleandbookministry.exceptions;

public class CategoryDoesNotExist extends RuntimeException {
    public CategoryDoesNotExist(String message) {
        super(message);
    }

    public CategoryDoesNotExist() {
    }
}
