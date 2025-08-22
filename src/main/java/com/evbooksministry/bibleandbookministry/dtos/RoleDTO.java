package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.enums.DeleteYn;

import java.sql.Timestamp;
import java.util.UUID;

public record RoleDTO(
        UUID roleId,
        String roleName,
        String roleCode,
        String description,
        Timestamp createdAt,
        Timestamp updatedAt,
        DeleteYn deleteYn
) {
}
