package com.evbooksministry.bibleandbookministry.mappers;

import com.evbooksministry.bibleandbookministry.dtos.RoleDTO;
import com.evbooksministry.bibleandbookministry.models.Role;
import com.evbooksministry.bibleandbookministry.models.Users;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO convertToDTO(Role role);

    Role convertToEntity(RoleDTO roleDTO);

    UUID map(Users value);

    Users map(UUID value);
}
