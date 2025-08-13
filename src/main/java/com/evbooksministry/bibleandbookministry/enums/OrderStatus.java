package com.evbooksministry.bibleandbookministry.enums;

public enum OrderStatus {
    PENDING("Order is pending"),
    IN_CART("Order is in cart"),
    PAID("Order is paid"),
    CANCELLED("Order is cancelled"),
    SHIPPED("Order is shipped"),
    DELIVERED("Order is delivered"),
    FAILED("Order failed");

    private final String value;
    OrderStatus(String value){
        this.value = value;
    }
}
