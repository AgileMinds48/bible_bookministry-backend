package com.evbooksministry.bibleandbookministry.dtos;

public record EmailValidationResponse(
        Boolean success,
        String successMessage
) {
}
