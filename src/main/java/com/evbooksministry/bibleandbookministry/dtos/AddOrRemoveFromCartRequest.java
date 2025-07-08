package com.evbooksministry.bibleandbookministry.dtos;

import java.util.UUID;

public record AddOrRemoveFromCartRequest(
        UUID bookId,
        Integer quantity
) {
}
