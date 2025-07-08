package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.enums.BookCategory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public record BookDTO(
        UUID bookId,
        String bookTitle,
        String bookDescription,
        BigDecimal bookPrice,
        Integer quantity,
        BigDecimal bookValue,
        Timestamp createdOn,
        Timestamp updatedOn,
        Integer amountSold,
        BookCategory bookCategory,
        Integer amountInStock
) {
}
