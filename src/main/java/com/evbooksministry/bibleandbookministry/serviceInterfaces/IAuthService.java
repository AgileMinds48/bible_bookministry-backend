package com.evbooksministry.bibleandbookministry.serviceInterfaces;

import com.evbooksministry.bibleandbookministry.dtos.*;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService{


    RegistrationResponse userRegistration(UserDTO registrationDTO);



    EmailValidationResponse sendValidationEmail(RequestEmailValidation requestEmailValidation);



    EmailValidationResponse validateUserEmail(EmailValidationRequest request);



    LoginResponse userLogin(LoginRequest loginRequest, HttpServletResponse response);
}

