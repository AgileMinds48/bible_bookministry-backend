package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.enums.BookCategory;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateBook(
        UUID bookId,
        BookCategory bookCategory,
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
                this.bookCategory,
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
