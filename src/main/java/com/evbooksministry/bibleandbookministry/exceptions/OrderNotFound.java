package com.evbooksministry.bibleandbookministry.exceptions;

public class OrderNotFound extends RuntimeException {
    public OrderNotFound(String message) {
        super(message);
    }

    public OrderNotFound() {
    }
}
