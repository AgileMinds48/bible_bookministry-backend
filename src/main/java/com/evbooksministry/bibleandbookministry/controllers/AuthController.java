package com.evbooksministry.bibleandbookministry.controllers;


import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.dtos.LoginRequest;
import com.evbooksministry.bibleandbookministry.dtos.UserDTO;
import com.evbooksministry.bibleandbookministry.services.AuthService;
import com.evbooksministry.bibleandbookministry.services.CloudinaryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> userSignup(
            @RequestPart("userInfo") String apiRequest,
            @RequestPart("userImage")MultipartFile userImage
            ) throws JsonProcessingException {
        UserDTO registrationRequest = objectMapper.readValue(
                apiRequest,
                UserDTO.class
        );
        String userImageURL =
                cloudinaryService.uploadFile(userImage);
        registrationRequest.setProfilePictureURL(userImageURL);
        return new ResponseEntity<>(
                authService.userRegistration(registrationRequest),
                HttpStatus.OK
        );
    }
}