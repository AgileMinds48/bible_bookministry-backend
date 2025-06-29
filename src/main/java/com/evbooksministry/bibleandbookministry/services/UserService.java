package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.dtos.LoginRequest;
import com.evbooksministry.bibleandbookministry.dtos.LoginResponse;
import com.evbooksministry.bibleandbookministry.dtos.RegistrationResponse;
import com.evbooksministry.bibleandbookministry.dtos.UserDTO;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.evbooksministry.bibleandbookministry.serviceInterfaces.UserServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface {
    private final JWTService jwtService;
    private final UserRepository userRepository;

    public UserService(JWTService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public LoginResponse userLogin(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public RegistrationResponse userRegistration(UserDTO registrationDTO) {
        return null;
    }
}
