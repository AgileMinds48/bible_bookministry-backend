package com.evbooksministry.bibleandbookministry.dtos;

import java.util.UUID;

public record UpdateBookDetails(
        UUID bookId,
        String field,
        Object value

) {
    public UpdateBookDetails newDetails(UUID bookId){
        return new UpdateBookDetails(bookId, this.field, this.value);
    }
}
