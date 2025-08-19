package com.evbooksministry.bibleandbookministry.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateBook(
        UUID bookId,
        String categoryName,
        String bookAuthor,
        String bookDescription,
        String bookTitle,
        BigDecimal bookPrice,
        Integer amountInStock,
        String fieldToUpdate,
        Integer bookQuantity
) {
    public UpdateBook bookDetails(UUID bookId){
        return new UpdateBook(
                bookId,
                this.categoryName,
                this.bookAuthor,
                this.bookDescription,
                this.bookTitle,
                this.bookPrice,
                this.amountInStock,
                this.fieldToUpdate,
                this.bookQuantity
        );
    }
}
