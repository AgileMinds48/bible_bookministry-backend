package com.evbooksministry.bibleandbookministry.controllers;

import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.dtos.BookDTO;
import com.evbooksministry.bibleandbookministry.exceptions.BookNotFound;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.evbooksministry.bibleandbookministry.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    public BookController(BookService bookService,
                             ObjectMapper objectMapper,
                             HttpServletRequest request,
                             JWTService jwtService,
                             UserRepository userRepository) {
        this.bookService = bookService;
        this.objectMapper = objectMapper;
        this.request = request;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @GetMapping("/all-books")
    public ResponseEntity<?> getAllProducts(Pageable pageable) {
        try{
            Page<BookDTO> products = bookService.getAllBooks(pageable);
            return ResponseEntity.ok(products);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-category/{category}")
    public ResponseEntity<?> getByCategory(@PathVariable String category, Pageable pageable) {
        try{

            System.out.println(category);
            Page<BookDTO> products = bookService.getProductsByCategory(category, pageable);
            return new ResponseEntity<>(products, HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("an error occurred.");
        }
    }

    @GetMapping("/get-book/{bookId}")
    public ResponseEntity<?> getBook(@PathVariable UUID bookId) {
        Optional<BookDTO> product = bookService.getBookById(bookId);
        if (product.isPresent()) {
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        }else{
            throw new BookNotFound();
        }
    }
}
