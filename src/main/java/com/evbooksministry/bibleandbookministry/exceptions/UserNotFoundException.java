package com.evbooksministry.bibleandbookministry.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

  public UserNotFoundException() {
  }
}
