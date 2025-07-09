package com.evbooksministry.bibleandbookministry.Services;

import com.evbooksministry.bibleandbookministry.config.EmailService;
import com.evbooksministry.bibleandbookministry.dtos.AdminDTO;
import com.evbooksministry.bibleandbookministry.dtos.BookDTO;
import com.evbooksministry.bibleandbookministry.dtos.UserDTO;
import com.evbooksministry.bibleandbookministry.mappers.BookMapper;
import com.evbooksministry.bibleandbookministry.mappers.UserMapper;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.BookRepository;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.evbooksministry.bibleandbookministry.services.AdminService;
import com.evbooksministry.bibleandbookministry.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookService bookService;

    @InjectMocks
    private AdminService adminService;

    // Initialize mocks before each test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for getAllUsers: should return a list of UserDTOs mapped from Users
    @Test
    void testGetAllUsersReturnsUserDTOList() {
        Users user = mock(Users.class);
        UserDTO userDTO = mock(UserDTO.class);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.userEntityToUserDTO(user)).thenReturn(userDTO);
        List<UserDTO> result = adminService.getAllUsers();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(userDTO, result.get(0));
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).userEntityToUserDTO(user);
    }

    // Test for getAllBooks: should delegate to BookService and return a Page<BookDTO>
    @Test
    void testGetAllBooksReturnsPageOfBookDTO() {
        Pageable pageable = mock(Pageable.class);
        Page<BookDTO> bookDTOPage = mock(Page.class);
        when(bookService.getAllBooks(pageable)).thenReturn(bookDTOPage);
        Page<BookDTO> result = adminService.getAllBooks(pageable);
        assertNotNull(result);
        assertSame(bookDTOPage, result);
        verify(bookService, times(1)).getAllBooks(pageable);
    }

    // Test for getEnabledUser: should return a list of active Users
    @Test
    void testGetEnabledUserReturnsActiveUsers() {
        Users user = mock(Users.class);
        List<Users> activeUsers = Collections.singletonList(user);
        when(userRepository.findByIsActive()).thenReturn(activeUsers);
        List<Users> result = adminService.getEnabledUser();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(user, result.get(0));
        verify(userRepository, times(1)).findByIsActive();
    }

    // Test for getUserById: should fetch a user by ID and map to UserDTO
    @Test
    void testGetUserByIdReturnsUserDTO() {
        UUID userId = UUID.randomUUID();
        Users user = mock(Users.class);
        UserDTO userDTO = mock(UserDTO.class);
        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(userMapper.userEntityToUserDTO(user)).thenReturn(userDTO);
        UserDTO result = adminService.getUserById(userId);
        assertNotNull(result);
        assertSame(userDTO, result);
        verify(userRepository, times(1)).findByUserId(userId);
        verify(userMapper, times(1)).userEntityToUserDTO(user);
    }

    // Test for registerAdmin: should encode password, save user, and map to UserDTO
    @Test
    void testRegisterAdminReturnsUserDTO() {
        AdminDTO adminDTO = new AdminDTO("admin@email.com", "First", "Last", "adminUser", "1234567890", "password");
        Users user = mock(Users.class);
        UserDTO userDTO = mock(UserDTO.class);
        when(passwordEncoder.encode(adminDTO.password())).thenReturn("encodedPassword");
        // We want to capture the Users object passed to save, so we use any(Users.class)
        when(userRepository.save(any(Users.class))).thenReturn(user);
        when(userMapper.userEntityToUserDTO(any(Users.class))).thenReturn(userDTO);
        UserDTO result = adminService.registerAdmin(adminDTO);
        assertNotNull(result);
        assertSame(userDTO, result);
        verify(passwordEncoder, times(1)).encode(adminDTO.password());
        verify(userRepository, times(1)).save(any(Users.class));
        verify(userMapper, times(1)).userEntityToUserDTO(any(Users.class));
    }
}

