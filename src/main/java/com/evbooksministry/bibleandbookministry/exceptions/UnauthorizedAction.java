package com.evbooksministry.bibleandbookministry.exceptions;

public class UnauthorizedAction extends RuntimeException {
    public UnauthorizedAction(String message) {
        super(message);
    }

    public UnauthorizedAction() {
    }
}
