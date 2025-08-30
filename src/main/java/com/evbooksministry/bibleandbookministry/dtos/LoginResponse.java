package com.evbooksministry.bibleandbookministry.dtos;

public record LoginResponse(
        boolean success,
        String userRole,
        String userName,
        String email,
        String token
) {
}
