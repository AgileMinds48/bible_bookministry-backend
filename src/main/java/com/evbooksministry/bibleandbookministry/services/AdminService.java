package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.config.EmailService;
import com.evbooksministry.bibleandbookministry.dtos.AdminDTO;
import com.evbooksministry.bibleandbookministry.dtos.BookDTO;
import com.evbooksministry.bibleandbookministry.dtos.UserDTO;
import com.evbooksministry.bibleandbookministry.enums.UserRole;
import com.evbooksministry.bibleandbookministry.mappers.BookMapper;
import com.evbooksministry.bibleandbookministry.mappers.UserMapper;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.BookRepository;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;
    private final BookService bookService;

    public AdminService(UserRepository userRepository, EmailService emailService, BookRepository bookRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, BookMapper bookMapper, BookService bookService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
        this.bookService = bookService;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::userEntityToUserDTO)
                .toList();
    }

    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }


    public List<Users> getEnabledUser() {
        return userRepository.findByIsActive();
    }


    public UserDTO getUserById(UUID id) {
        return userMapper.userEntityToUserDTO(userRepository.findByUserId(id));
    }

    public UserDTO registerAdmin(AdminDTO admin) {
        Users user = new Users();
        user.setEmail(admin.email());
        user.setUserName(admin.userName());
        user.setPhoneNumber(admin.phone());
        user.setUserRole(UserRole.ADMIN);
        user.setPassword(passwordEncoder.encode(admin.password()));
        user.setFirstName(admin.firstname());
        user.setLastName(admin.lastname());
        user.setActive(true);
        user.setEmailValid(true);
        userRepository.save(user);

        return userMapper.userEntityToUserDTO(user);
    }
}
