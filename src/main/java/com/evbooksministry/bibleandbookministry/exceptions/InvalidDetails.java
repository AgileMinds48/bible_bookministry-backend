package com.evbooksministry.bibleandbookministry.exceptions;

public class InvalidDetails extends RuntimeException{
    public InvalidDetails() {
    }

    public InvalidDetails(String message) {
        super(message);
    }
}
