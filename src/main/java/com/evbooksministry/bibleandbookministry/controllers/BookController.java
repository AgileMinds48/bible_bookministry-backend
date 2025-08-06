package com.evbooksministry.bibleandbookministry.controllers;

import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.dtos.BookDTO;
import com.evbooksministry.bibleandbookministry.dtos.UpdateBook;
import com.evbooksministry.bibleandbookministry.dtos.UpdateBookDetails;
import com.evbooksministry.bibleandbookministry.dtos.UpdateBookMedia;
import com.evbooksministry.bibleandbookministry.exceptions.BookNotFound;
import com.evbooksministry.bibleandbookministry.exceptions.InvalidDetails;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.evbooksministry.bibleandbookministry.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @DeleteMapping("/remove-book/{productId}")
    public ResponseEntity<?> removeProduct(@PathVariable UUID productId) {
        try {
            bookService.deleteProduct(productId);
        } catch (BookNotFound e) {
            throw new BookNotFound();
        }
        return new ResponseEntity<>("Product removed successfully", HttpStatus.OK);
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

    @GetMapping("/get-book/{bookId}")
    public ResponseEntity<?> getBook(@PathVariable String bookId) {
        UUID parsedBookId = UUID.fromString(bookId);
        Optional<BookDTO> product = bookService.getBookById(parsedBookId);
        if (product.isPresent()) {
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        }else{
            throw new BookNotFound();
        }
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
}
