package com.evbooksministry.bibleandbookministry;

import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.config.UserPrincipal;
import com.evbooksministry.bibleandbookministry.dtos.LoginRequest;
import com.evbooksministry.bibleandbookministry.dtos.LoginResponse;
import com.evbooksministry.bibleandbookministry.enums.UserRole;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.evbooksministry.bibleandbookministry.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Test class for AuthService, using JUnit 5 and Mockito for mocking dependencies
class AuthServiceTest {
    @Mock
    private JWTService jwtService; // Mocked JWT token service
    @Mock
    private UserRepository userRepository; // Mocked user repository
    @Mock
    private AuthenticationManager authenticationManager; // Mocked authentication manager
    @Mock
    private PasswordEncoder passwordEncoder; // Mocked password encoder
    @Mock
    private HttpServletResponse httpServletResponse; // Mocked HTTP response for setting cookies

    @InjectMocks
    private AuthService authService; // The service under test, with mocks injected

    // Initialize mocks before each test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for userLogin: should authenticate, generate JWT, and set cookie on success
    @Test
    void testUserLoginSuccess() {
        // Arrange: set up test data and mock behaviors
        String usernameOrEmail = "testuser";
        String password = "password";
        UserRole userRole = UserRole.CUSTOMER; // Use CUSTOMER as per enum
        UUID userId = UUID.randomUUID();
        String accessToken = "mocked.jwt.token";
        LoginRequest loginRequest = mock(LoginRequest.class);
        when(loginRequest.usernameOrEmail()).thenReturn(usernameOrEmail);
        when(loginRequest.password()).thenReturn(password);

        // Mock Users and UserPrincipal to simulate authenticated user
        Users users = mock(Users.class);
//        when(users.getUserRole()).thenReturn(userRole);
        when(users.getUserId()).thenReturn(userId);
        UserPrincipal userPrincipal = mock(UserPrincipal.class);
        when(userPrincipal.getUser()).thenReturn(users);

        // Mock Authentication and AuthenticationManager
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Mock JWTService to return a fake token
//        when(jwtService.generateAccessToken(usernameOrEmail, userRole, userId)).thenReturn(accessToken);

        // Act: call the method under test
        LoginResponse response = authService.userLogin(loginRequest, httpServletResponse);

        // Assert: verify the response and interactions
        assertNotNull(response); // Response should not be null
        assertTrue(response.success()); // Login should be successful
        assertEquals(userRole, response.userRole()); // User role should match
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class)); // Authentication should be attempted
//        verify(jwtService, times(1)).generateAccessToken(usernameOrEmail, userRole, userId); // Token should be generated
        verify(httpServletResponse, times(1)).setHeader(eq("Set-Cookie"), contains("JWTAccess_token")); // Cookie should be set
    }

    // Test for userRegistration: should save new user and return success response
    @Test
    void testUserRegistrationSuccess() {
        // Arrange: set up test data and mock behaviors
        String email = "newuser@email.com";
        String encodedPassword = "encodedPassword";
        com.evbooksministry.bibleandbookministry.dtos.UserDTO userDTO = mock(com.evbooksministry.bibleandbookministry.dtos.UserDTO.class);
        when(userDTO.email()).thenReturn(email);
        when(userDTO.password()).thenReturn("plainPassword");
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty()); // No existing user
        when(passwordEncoder.encode("plainPassword")).thenReturn(encodedPassword); // Password encoding
        // save returns the user, but we don't use the return value in the service
        when(userRepository.save(any(com.evbooksministry.bibleandbookministry.models.Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: call the method under test
        var response = authService.userRegistration(userDTO);

        // Assert: verify the response and interactions
        assertNotNull(response); // Response should not be null
        assertTrue(response.success()); // Registration should be successful
        // Check that the response contains the expected success message
        assertEquals("user account created successfully", response.successMessage());
        verify(userRepository, times(1)).findByEmail(email); // Email check should be performed
        verify(passwordEncoder, times(1)).encode("plainPassword"); // Password should be encoded
        verify(userRepository, times(1)).save(any(com.evbooksministry.bibleandbookministry.models.Users.class)); // User should be saved
    }
} 