package com.evbooksministry.bibleandbookministry.exceptions;

public class UnAuthorizedAddition extends RuntimeException {
    public UnAuthorizedAddition(String message) {
        super(message);
    }

    public UnAuthorizedAddition() {
    }
}
