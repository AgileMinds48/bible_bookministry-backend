package com.evbooksministry.bibleandbookministry.exceptions;

public class EmployeeNotFound extends RuntimeException {
    public EmployeeNotFound(String message) {
        super(message);
    }

    public EmployeeNotFound() {
    }
}
