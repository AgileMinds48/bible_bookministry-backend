package com.evbooksministry.bibleandbookministry.mappers;

import com.evbooksministry.bibleandbookministry.dtos.UserDTO;
import com.evbooksministry.bibleandbookministry.models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    Users userDTOToUser(UserDTO userDTO);

    @Mapping(target = "password", ignore = true)
    UserDTO userEntityToUserDTO(Users user);
}
