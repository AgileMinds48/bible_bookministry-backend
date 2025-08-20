package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.config.EmailService;
import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.config.OTPService;
import com.evbooksministry.bibleandbookministry.config.UserPrincipal;
import com.evbooksministry.bibleandbookministry.dtos.*;
import com.evbooksministry.bibleandbookministry.enums.UserRole;
import com.evbooksministry.bibleandbookministry.enums.UserStatus;
import com.evbooksministry.bibleandbookministry.exceptions.InvalidEmail;
import com.evbooksministry.bibleandbookministry.exceptions.UserAlreadyExists;
import com.evbooksministry.bibleandbookministry.exceptions.UserNotFound;
import com.evbooksministry.bibleandbookministry.models.Customer;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.CustomerRepository;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.evbooksministry.bibleandbookministry.serviceInterfaces.IAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AuthService implements IAuthService {
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final OTPService oTPService;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;

    public AuthService(JWTService jwtService,
                       UserRepository userRepository,
                       AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, OTPService oTPService, CustomerRepository customerRepository, EmailService emailService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.oTPService = oTPService;
        this.customerRepository = customerRepository;
        this.emailService = emailService;
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

        if (!validateEmailString(registrationDTO.email())){
            throw new InvalidEmail();
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
                .isActive(true)
                .isEmailValid(false)
                .build();

        userRepository.save(newUser);
        Customer customer = new Customer();
        customer.setUser(newUser);

        customerRepository.save(customer);
        return new RegistrationResponse(
                true,
                "user account created successfully"

        );

    }

    public EmailValidationResponse sendValidationEmail(RequestEmailValidation request){
        Optional<Users> unverifiedUser = userRepository.findByUserName(request.username());

        if (unverifiedUser.isEmpty()){
            throw new UserNotFound();
        }
        Users user = unverifiedUser.get();
        String otp = oTPService.generateAndStoreOTP(user.getEmail());
        System.out.println("otp: " + otp);
        EmailRequest emailRequest = new EmailRequest(
                user.getEmail(),
                "Email verification and account activation"
        );
        Context context = new Context();
        context.setVariable("username", request.username());
        context.setVariable("otp", otp);
        emailService.sendEmail(emailRequest, "EmailValidation", context);
        return new EmailValidationResponse(
                true,
                "OTP sent via email."
        );
    }

    public EmailValidationResponse validateUserEmail(EmailValidationRequest request){
        Optional<Users> user = userRepository.findByEmail(request.email());
        if (!oTPService.verifyOTP(request.email(), request.otp())){
            return new EmailValidationResponse(
                    false,
                    "Invalid email or OTP. Please try again."
            );
        }
        if (user.isPresent()){
            Users validUser = user.get();
            validUser.setEmailValid(true);
            validUser.setUserStatus(UserStatus.ACTIVE);
            userRepository.save(validUser);
            return new EmailValidationResponse(
                    true,
                    "User email verified successfully."
            );
        }
        throw new UserNotFound();
    }



    private boolean validateEmailString(String email){
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return patternMatches(email,regexPattern);
    }

    private boolean patternMatches(String email, String regexPattern){
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
}
