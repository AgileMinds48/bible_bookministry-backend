package com.evbooksministry.bibleandbookministry.dtos;

import java.util.UUID;

public record PaymentRequest(
        String email,
        String amount,
        UUID reference
) {
}
