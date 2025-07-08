package com.evbooksministry.bibleandbookministry.dtos;

import java.math.BigDecimal;

public record PaymentResponse(
        boolean status,
        String message,
        Data data
) {
    public record Data(
            String authorization_url,
            String access_code,
            String reference,
            BigDecimal amount
    ) {

    }
}
