package com.evbooksministry.bibleandbookministry.serviceInterfaces;

import com.evbooksministry.bibleandbookministry.dtos.UserDTO;
import com.evbooksministry.bibleandbookministry.dtos.ChangePasswordRequest;

import java.util.UUID;

public interface IUserService {
    void updateProfile(UUID userId, UserDTO dto);
    void changePassword(UUID userId, ChangePasswordRequest request);
    void deactivateAccount(UUID userId);
}
