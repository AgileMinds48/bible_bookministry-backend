package com.evbooksministry.bibleandbookministry.dtos;

public record AdminDTO(
        String email,
        String firstname,
        String lastname,
        String userName,
        String phone,
        String password
) {
}
