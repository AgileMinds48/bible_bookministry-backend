package com.evbooksministry.bibleandbookministry.dtos;

import java.util.UUID;

public record RoleDTO(
    UUID roleId,
    String roleName,
    String roleCode,
    String description,
    boolean isActive
) {}
