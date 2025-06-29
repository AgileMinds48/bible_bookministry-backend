package com.evbooksministry.bibleandbookministry.enums;

public enum UserRole {
    ADMIN("admin"), CUSTOMER("customer");

    private final String value;

    UserRole(String value){
        this.value = value;
    }
}
