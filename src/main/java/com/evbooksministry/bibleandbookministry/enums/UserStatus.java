package com.evbooksministry.bibleandbookministry.enums;

public enum UserStatus {
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String value;

    UserStatus(String value){
        this.value = value;
    }
}
