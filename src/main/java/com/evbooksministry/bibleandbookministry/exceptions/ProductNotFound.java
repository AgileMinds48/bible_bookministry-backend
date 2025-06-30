package com.evbooksministry.bibleandbookministry.exceptions;

public class ProductNotFound extends RuntimeException {
    public ProductNotFound(String message) {
        super(message);
    }

    public ProductNotFound() {
    }
}
