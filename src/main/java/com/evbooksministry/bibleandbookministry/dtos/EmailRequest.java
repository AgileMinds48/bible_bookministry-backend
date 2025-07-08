package com.evbooksministry.bibleandbookministry.dtos;

public record EmailRequest(
        String recipient,
        String subject
) {
}
