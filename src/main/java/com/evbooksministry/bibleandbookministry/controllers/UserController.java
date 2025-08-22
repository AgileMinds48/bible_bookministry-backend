package com.evbooksministry.bibleandbookministry.controllers;

import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.dtos.AddOrRemoveFromCartRequest;
import com.evbooksministry.bibleandbookministry.dtos.BookDTO;
import com.evbooksministry.bibleandbookministry.services.BookService;
import com.evbooksministry.bibleandbookministry.services.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final BookService bookService;
    private final JWTService jwtService;
    private final CartService cartService;

    public UserController(BookService bookService, JWTService jwtService, CartService cartService) {
        this.bookService = bookService;
        this.jwtService = jwtService;
        this.cartService = cartService;
    }

    @GetMapping("/book-catalog")
    public Page<BookDTO> getAllBooks(Pageable pageable){
        return bookService.getAllBooks(pageable);
    }

    @GetMapping("/get-book/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable UUID bookId){
        return new ResponseEntity<>(bookService.getBookById(bookId), HttpStatus.OK);
    }

    @PostMapping("/cart/add")
    public ResponseEntity<?> addItemToCart(
            @RequestBody AddOrRemoveFromCartRequest request, HttpServletRequest httpServletRequest
            ){
        UUID customerId = extractUserId(httpServletRequest);
        return new ResponseEntity<>(cartService.addItemToCart(request, customerId), HttpStatus.OK);
    }



    private UUID extractUserId(HttpServletRequest request){
        String authToken = getTokenFromCookie(request.getCookies());
        return jwtService.extractCustomerId(authToken);
    }

    private String getTokenFromCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWTAccess_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
