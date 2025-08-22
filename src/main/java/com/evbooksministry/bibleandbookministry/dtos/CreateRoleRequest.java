package com.evbooksministry.bibleandbookministry.dtos;

public record CreateRoleRequest(
    String roleName,
    String roleCode,
    String description
) {}
