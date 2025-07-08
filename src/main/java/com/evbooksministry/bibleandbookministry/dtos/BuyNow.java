package com.evbooksministry.bibleandbookministry.dtos;

import java.util.UUID;

public record BuyNow(
        UUID userID,
        UUID bookId,
        int quantity
) {
    public BuyNow newUserId(UUID userId){
        return new BuyNow(userId,this.bookId, this.quantity);
    }
}