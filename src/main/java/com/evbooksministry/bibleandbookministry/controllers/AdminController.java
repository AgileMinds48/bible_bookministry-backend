package com.evbooksministry.bibleandbookministry.controllers;

import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.dtos.*;
import com.evbooksministry.bibleandbookministry.exceptions.BookNotFound;
import com.evbooksministry.bibleandbookministry.exceptions.InvalidDetails;
import com.evbooksministry.bibleandbookministry.exceptions.UnauthorizedAction;
import com.evbooksministry.bibleandbookministry.exceptions.UserNotFound;
import com.evbooksministry.bibleandbookministry.mappers.BookMapper;
import com.evbooksministry.bibleandbookministry.models.Role;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.BookRepository;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.evbooksministry.bibleandbookministry.services.AdminService;
import com.evbooksministry.bibleandbookministry.services.BookService;
import com.evbooksministry.bibleandbookministry.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final CategoryService categoryService;


    @Autowired
    public AdminController(AdminService adminService,
                           BookRepository bookRepository,
                           BookMapper bookMapper, BookService bookService, HttpServletRequest request, JWTService jwtService, UserRepository userRepository, ObjectMapper objectMapper, CategoryService categoryService) {
        this.adminService = adminService;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookService = bookService;
        this.request = request;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.categoryService = categoryService;
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
            UUID userId = jwtService.getAdminId(request);

            Users user = userRepository.findById(userId)
                    .orElseThrow(UserNotFound::new);

            Role userRole = user.getRoleId();
            if (userRole.getRoleName().equalsIgnoreCase("customer")) {
                throw new UnauthorizedAction();
            }
            AddBookRequest request = objectMapper.readValue(addBookRequest, AddBookRequest.class);
            System.out.println("new book: " + request);
            return new ResponseEntity<>(bookService.addNewBook(request, bookImage, user), HttpStatus.OK);
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

    @PatchMapping("/update-details")
    public ResponseEntity<?> updateBookDetails(@RequestBody UpdateBookDetails request) throws InvalidDetails {
        try{
            return new ResponseEntity<>(bookService.updateBookDetails(request), HttpStatus.OK);
        }catch (InvalidDetails e){
            throw new InvalidDetails();
        }
    }

    @PatchMapping("/update-media")
    public ResponseEntity<?> updateProductMedia(
            @RequestPart("productId")String productId,
            @RequestPart("media")MultipartFile[] media
    ){
        try{
            UUID product = UUID.fromString(productId);
            UpdateBookMedia updateProductMedia = new UpdateBookMedia(
                    product,
                    media
            );
            return new ResponseEntity<>(bookService.updateProductMedia(updateProductMedia), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(
            @RequestPart("product")String updateDetails,
            @RequestPart("media")MultipartFile[] files
    )  {
        try{
            UpdateBook update = new ObjectMapper().readValue(updateDetails, UpdateBook.class);
            return new ResponseEntity<>(bookService.updateProduct(update, files), HttpStatus.OK);
        }catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove-book/{productId}")
    public ResponseEntity<?> removeProduct(@PathVariable UUID productId) {
        try {
            bookService.deleteProduct(productId);
        } catch (BookNotFound e) {
            throw new BookNotFound();
        }
        return new ResponseEntity<>("Product removed successfully", HttpStatus.OK);
    }


    @GetMapping("/books/get-available")
    public ResponseEntity<?> getTotalAvailableBooks(){
        return new ResponseEntity<>(adminService.getTotalAvailableBooks(), HttpStatus.OK);
    }

    @GetMapping("/orders/get-total")
    public ResponseEntity<?> getTotalAvailableOrders(){
        return new ResponseEntity<>(adminService.getTotalOrders(), HttpStatus.OK);
    }

    @GetMapping("/orders/total-sales")
    public ResponseEntity<?> getTotalSales(){
        return new ResponseEntity<>(adminService.getTotalSales(), HttpStatus.OK);
    }

    @GetMapping("/books/low-stock")
    public ResponseEntity<?> getBooksLowInStock(){
        return new ResponseEntity<>(adminService.getBooksLowInStock(), HttpStatus.OK);
    }

    @GetMapping("/orders/customer/{customerId}")
    public ResponseEntity<?> viewCustomerOrderHistory(@PathVariable UUID customerId){
        return new ResponseEntity<>(adminService.viewCustomerOrderHistory(customerId), HttpStatus.OK);
    }


    @GetMapping("/books/highest-selling")
    public ResponseEntity<?> getHighestSellingBooks(){
        return new ResponseEntity<>(adminService.getHighestSellingBooks(), HttpStatus.OK);
    }

    @PostMapping("/category/create-defaults")
    public ResponseEntity<?> createDefaultCategories(@RequestBody List<CategoryDTO> dtoList){
        return new ResponseEntity<>(categoryService.createDefaultCategories(dtoList), HttpStatus.OK);
    }

    @PostMapping("/category/new")
    public ResponseEntity<?> createNewCategory(@RequestBody CategoryDTO newCategory){
        return new ResponseEntity<>(categoryService.createNewBookCategory(newCategory), HttpStatus.CREATED);
    }
}
