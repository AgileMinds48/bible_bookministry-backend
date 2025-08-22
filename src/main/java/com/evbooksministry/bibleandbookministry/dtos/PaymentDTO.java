package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.enums.PaymentMethod;
import com.evbooksministry.bibleandbookministry.enums.PaymentStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record PaymentDTO(
        BigDecimal amount,
        PaymentStatus status,
        Timestamp paymentDate,
        PaymentMethod paymentMethod
) {
}
