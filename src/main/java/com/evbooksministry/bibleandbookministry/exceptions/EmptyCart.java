package com.evbooksministry.bibleandbookministry.exceptions;

public class EmptyCart extends RuntimeException {
    public EmptyCart(String message) {
        super(message);
    }

    public EmptyCart() {
    }
}
