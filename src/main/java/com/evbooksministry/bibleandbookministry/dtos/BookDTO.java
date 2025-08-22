package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.models.Category;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public record BookDTO(
        UUID bookId,
        String bookTitle,
        String bookDescription,
        String bookAuthor,
        BigDecimal bookPrice,
        Integer quantity,
        BigDecimal bookValue,
        Timestamp createdOn,
        Timestamp updatedOn,
        Integer amountSold,
        Category bookCategory,
        Integer amountInStock
) {
}
