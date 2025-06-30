package com.evbooksministry.bibleandbookministry.serviceInterfaces;

import com.evbooksministry.bibleandbookministry.dtos.LoginRequest;
import com.evbooksministry.bibleandbookministry.dtos.LoginResponse;
import com.evbooksministry.bibleandbookministry.dtos.RegistrationResponse;
import com.evbooksministry.bibleandbookministry.dtos.UserDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthServiceInterface {

    /**
     * User Login
     * @return the login response containing the user's details and access token
     * @param loginRequest, the user's login credentials
     */
    LoginResponse userLogin(LoginRequest loginRequest, HttpServletResponse response);

    /**
     * @params UserDTO
     * @returns Registration response and enrols a user into the system
     */
    RegistrationResponse userRegistration(UserDTO registrationDTO);

}
