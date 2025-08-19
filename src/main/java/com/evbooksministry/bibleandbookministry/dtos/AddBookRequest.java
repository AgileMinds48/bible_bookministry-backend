package com.evbooksministry.bibleandbookministry.dtos;

import java.math.BigDecimal;

public record AddBookRequest(
        String bookTitle,
        String bookDescription,
        String bookAuthor,
        BigDecimal bookPrice,
        Integer quantity,
        BigDecimal bookValue,
        Integer amountInStock,
        String categoryName
) {
}
