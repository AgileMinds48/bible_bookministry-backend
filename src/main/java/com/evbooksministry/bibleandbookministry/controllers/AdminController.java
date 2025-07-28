package com.evbooksministry.bibleandbookministry.controllers;

import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.dtos.AddBookRequest;
import com.evbooksministry.bibleandbookministry.dtos.AdminDTO;
import com.evbooksministry.bibleandbookministry.dtos.BookDTO;
import com.evbooksministry.bibleandbookministry.dtos.UserDTO;
import com.evbooksministry.bibleandbookministry.enums.UserRole;
import com.evbooksministry.bibleandbookministry.exceptions.UnauthorizedAction;
import com.evbooksministry.bibleandbookministry.exceptions.UserNotFound;
import com.evbooksministry.bibleandbookministry.mappers.BookMapper;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.BookRepository;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.evbooksministry.bibleandbookministry.services.AdminService;
import com.evbooksministry.bibleandbookministry.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/admin/")
public class AdminController {
    private final AdminService adminService;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookService bookService;
    private final HttpServletRequest request;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;


    @Autowired
    public AdminController(AdminService adminService,
                           BookRepository bookRepository,
                           BookMapper bookMapper, BookService bookService, HttpServletRequest request, JWTService jwtService, UserRepository userRepository, ObjectMapper objectMapper) {
        this.adminService = adminService;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookService = bookService;
        this.request = request;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping("register")
    public UserDTO register(@RequestBody AdminDTO admin) {
        return adminService.registerAdmin(admin);
    }

    @GetMapping("get-users")
    public List<UserDTO> getUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("get-books")
    public Page<BookDTO> getBooks(Pageable pageable) {
        return adminService.getAllBooks(pageable);
    }

    @PostMapping("add-book")
    public ResponseEntity<?> addProduct(
            @RequestPart("book") String addBookRequest,
            @RequestPart("bookMedia") MultipartFile[] bookImage

    ){
        try {
            String authHeader = request.getHeader("Authorization");
            String jwtToken = authHeader.substring(7);

            UUID userId = jwtService.extractAdminId(jwtToken);

            Users user = userRepository.findById(userId)
                    .orElseThrow(UserNotFound::new);


            UserRole userRole = user.getUserRole();
            if (userRole == UserRole.CUSTOMER) {
                throw new UnauthorizedAction();
            }
            AddBookRequest request = objectMapper.readValue(addBookRequest, AddBookRequest.class);
            System.out.println("new book: " + request);
            return new ResponseEntity<>(bookService.addNewBook(request, bookImage), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get-user/{userId}")
    public UserDTO getUserById(@PathVariable UUID userId) {
        System.out.println("user ID: " + userId);
        return adminService.getUserById(userId);
    }
}
