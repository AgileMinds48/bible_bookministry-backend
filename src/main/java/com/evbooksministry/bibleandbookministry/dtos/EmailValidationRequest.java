package com.evbooksministry.bibleandbookministry.dtos;

public record EmailValidationRequest(
        String email,
        String otp
) {
}
