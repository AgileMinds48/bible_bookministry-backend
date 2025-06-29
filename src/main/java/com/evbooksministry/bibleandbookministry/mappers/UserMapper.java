package com.evbooksministry.bibleandbookministry.mappers;

import com.evbooksministry.bibleandbookministry.dtos.UserDTO;
import com.evbooksministry.bibleandbookministry.models.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    Users userDTOToUser(UserDTO userDTO);

    UserDTO userEntityToUserDTO(Users user);
}
