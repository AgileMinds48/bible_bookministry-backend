package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.dtos.ChangePasswordRequest;
import com.evbooksministry.bibleandbookministry.dtos.UserDTO;
import com.evbooksministry.bibleandbookministry.serviceInterfaces.IUserService;

import java.util.UUID;

public class UserService implements IUserService {


    @Override
    public void updateProfile(UUID userId, UserDTO dto) {

    }

    @Override
    public void changePassword(UUID userId, ChangePasswordRequest request) {

    }

    @Override
    public void deactivateAccount(UUID userId) {

    }
}
