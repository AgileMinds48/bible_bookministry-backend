package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.dtos.RoleDTO;
import com.evbooksministry.bibleandbookministry.serviceInterfaces.IRoleService;

import java.util.UUID;

public class RoleService implements IRoleService {
    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        return null;
    }

    @Override
    public void assignRole(UUID userId, String roleName) {

    }
}
