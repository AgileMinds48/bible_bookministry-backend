package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.config.UserPrincipal;
import com.evbooksministry.bibleandbookministry.dtos.LoginRequest;
import com.evbooksministry.bibleandbookministry.dtos.LoginResponse;
import com.evbooksministry.bibleandbookministry.dtos.RegistrationResponse;
import com.evbooksministry.bibleandbookministry.dtos.UserDTO;
import com.evbooksministry.bibleandbookministry.enums.UserRole;
import com.evbooksministry.bibleandbookministry.exceptions.UserAlreadyExists;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.evbooksministry.bibleandbookministry.serviceInterfaces.AuthServiceInterface;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService implements AuthServiceInterface {
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JWTService jwtService,
                       UserRepository userRepository,
                       AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public LoginResponse userLogin(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (loginRequest.usernameOrEmail(), loginRequest.password()));

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();


            Users users = userPrincipal.getUser();
            String accessToken = jwtService.generateAccessToken(loginRequest.usernameOrEmail(), users.getUserRole(), users.getUserId());
            System.out.println("access token: " + accessToken);
            ResponseCookie jwtCookie = ResponseCookie.from("JWTAccess_token", accessToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(3600)
                    .build();
            response.setHeader("Set-Cookie", jwtCookie.toString());
            System.out.println("jwt cookie " + jwtCookie);

            return new LoginResponse(
                 true,
                    users.getUserRole()
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("User entered wrong credentials");
        }
    }

    @Override
    public RegistrationResponse userRegistration(UserDTO registrationDTO) {
        Optional<Users> existingUser = userRepository.findByEmail(registrationDTO.email());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExists();
        }

        Users newUser = Users.builder()
                .firstName(registrationDTO.firstName())
                .lastName(registrationDTO.lastName())
                .userName(registrationDTO.userName())
                .userGender(registrationDTO.userGender())
                .password(passwordEncoder.encode(registrationDTO.password()))
                .email(registrationDTO.email())
                .phoneNumber(registrationDTO.phoneNumber())
                .userRole(UserRole.CUSTOMER)
                .city(registrationDTO.city())
                .country(registrationDTO.country())
                .state(registrationDTO.state())
                .createdAt(Timestamp.from(Instant.now()))
                .updatedAt(Timestamp.from(Instant.now()))
                .profilePictureURL(
                        registrationDTO.profilePictureURL() == null ?
                                "no picture" :
                                registrationDTO.profilePictureURL()
                )
                .isActive(true)
                .build();
        userRepository.save(newUser);
        return new RegistrationResponse(
                true,
                "user account created successfully"

        );

    }
}
