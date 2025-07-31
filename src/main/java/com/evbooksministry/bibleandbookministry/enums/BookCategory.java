package com.evbooksministry.bibleandbookministry.enums;

public enum BookCategory{
    RELIGIOUS("religious"),
    COMMENTARY("commentary"),
    CHURCH_HISTORY("Church History"),
    THEOLOGICAL("Theological"),
    COMMENTARIES("Commentaries"),
    BIBLES("Bibles"),
    CHILDREN("Children");

    private final String value;

    BookCategory(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }



}
