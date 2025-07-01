package com.evbooksministry.bibleandbookministry.enums;

public enum BookCategory{
    RELIGIOUS("religious"),
    COMMENTARY("commentary");
    private final String value;

    BookCategory(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
