package com.evbooksministry.bibleandbookministry.dtos;

public record LoginRequest(
        String usernameOrEmail,
        String password
) {
}
