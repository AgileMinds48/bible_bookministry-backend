package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.enums.Gender;

import java.sql.Timestamp;
import java.util.UUID;

public record UserDTO (
        UUID userId,
        String firstName,
        String lastName,
        String userName,
        Gender userGender,
        String password,
        String email,
        String phoneNumber,
        Timestamp createdAt,
        Timestamp updatedAt
) {
}
