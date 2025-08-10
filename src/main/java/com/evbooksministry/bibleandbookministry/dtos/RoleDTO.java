package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.enums.DeleteYn;
import com.evbooksministry.bibleandbookministry.models.Users;

import java.sql.Timestamp;

public record RoleDTO(
        String roleName,
        Users createdBy,
        Timestamp createdAt,
        Timestamp updatedAt,
        DeleteYn deleteYn
) {
}
