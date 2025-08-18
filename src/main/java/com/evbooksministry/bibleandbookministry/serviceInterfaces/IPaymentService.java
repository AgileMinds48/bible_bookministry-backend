package com.evbooksministry.bibleandbookministry.serviceInterfaces;

import com.evbooksministry.bibleandbookministry.dtos.PaymentDTO;
import com.evbooksministry.bibleandbookministry.dtos.PaymentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;
import java.util.UUID;

public interface IPaymentService {
    PaymentResponse processPayment(String userEmail, BigDecimal totalAmount) throws JsonProcessingException;

    void refundPayment(UUID paymentId);

    PaymentDTO getPayment(UUID paymentId);

    void deletePayment(UUID paymentId);
}
