package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.enums.BookCategory;

import java.math.BigDecimal;

public record AddBookRequest(
        String bookTitle,
        String bookDescription,
        BigDecimal bookPrice,
        Integer quantity,
        BigDecimal bookValue,
        Integer amountInStock,
        BookCategory bookCategory
) {
}
