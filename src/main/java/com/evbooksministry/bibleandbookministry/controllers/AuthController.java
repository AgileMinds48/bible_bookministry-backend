package com.evbooksministry.bibleandbookministry.controllers;


import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.dtos.EmailValidationRequest;
import com.evbooksministry.bibleandbookministry.dtos.LoginRequest;
import com.evbooksministry.bibleandbookministry.dtos.RequestEmailValidation;
import com.evbooksministry.bibleandbookministry.dtos.UserDTO;
import com.evbooksministry.bibleandbookministry.exceptions.InvalidEmail;
import com.evbooksministry.bibleandbookministry.exceptions.UserAlreadyExists;
import com.evbooksministry.bibleandbookministry.exceptions.UserNotFound;
import com.evbooksministry.bibleandbookministry.services.AuthService;
import com.evbooksministry.bibleandbookministry.services.CloudinaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final JWTService jwtService;
    private final AuthService authService;
    private final CloudinaryService cloudinaryService;
    private final ObjectMapper objectMapper;


    public AuthController(JWTService jwtService,
                          AuthService authService, CloudinaryService cloudinaryService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.authService = authService;
        this.cloudinaryService = cloudinaryService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return new ResponseEntity<>(
                authService.userLogin(loginRequest, response),
                HttpStatus.OK
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> userSignup(@RequestBody UserDTO apiRequest) {
        try {
            return new ResponseEntity<>(
                    authService.userRegistration(apiRequest),
                    HttpStatus.OK
            );
        } catch (UserAlreadyExists e) {
            throw new UserNotFound();
        } catch (InvalidEmail e){
            throw new InvalidEmail();
        }
    }

    @PostMapping("/request-validation")
    public ResponseEntity<?> requestEmailValidation(@RequestBody RequestEmailValidation requestEmailValidation){
        return new ResponseEntity<>(authService.sendValidationEmail(requestEmailValidation), HttpStatus.OK);
    }

    @PostMapping("/validate-email")
    public ResponseEntity<?> validateEmail(@RequestBody EmailValidationRequest request){
        return new ResponseEntity<>(authService.validateUserEmail(request), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> greeting(){
        return new ResponseEntity<>("Welcome To Bible and Book Ministry server.", HttpStatus.OK);
    }
}