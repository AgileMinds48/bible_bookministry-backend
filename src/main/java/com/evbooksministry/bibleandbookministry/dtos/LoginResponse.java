package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.enums.UserRole;

public record LoginResponse(
        boolean success,
        UserRole userRole
) {
}
