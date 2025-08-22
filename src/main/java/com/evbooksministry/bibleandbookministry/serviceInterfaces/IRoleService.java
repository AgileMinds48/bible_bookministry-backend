package com.evbooksministry.bibleandbookministry.serviceInterfaces;

import com.evbooksministry.bibleandbookministry.dtos.RoleDTO;

import java.util.UUID;

public interface IRoleService {
    RoleDTO createRole(RoleDTO roleDTO);
    void assignRole(UUID userId, String roleName);
}
