package com.evbooksministry.bibleandbookministry.controllers;

import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.dtos.AddOrRemoveFromCartRequest;
import com.evbooksministry.bibleandbookministry.exceptions.BookNotFound;
import com.evbooksministry.bibleandbookministry.exceptions.UserNotFoundException;
import com.evbooksministry.bibleandbookministry.services.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/cart")
public class CartController {
    private final CartService cartService;
    private final HttpServletRequest request;
    private final JWTService jwtService;

    public CartController(CartService cartService,
                          HttpServletRequest request,
                          JWTService jwtService) {
        this.cartService = cartService;
        this.request = request;
        this.jwtService = jwtService;
    }
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddOrRemoveFromCartRequest addRequest) {
        try{
            UUID userID = extractUserId(request);
            System.out.println("checkout user ID: " + userID);
            return new ResponseEntity<>(cartService.addItemToCart(addRequest, userID), HttpStatus.OK);

        }catch(UserNotFoundException e){
            throw new UserNotFoundException();
        }catch (BookNotFound e){
            throw new BookNotFound();
        }
    }

    @DeleteMapping("/remove-item")
    public ResponseEntity<?> removeFromCart(@RequestBody AddOrRemoveFromCartRequest removeRequest) {
        UUID userID = extractUserId(request);
        System.out.println("user Id in remove item from cart: " + userID);
        try{
            return new ResponseEntity<>(cartService.removeItemFromCart(removeRequest, userID), HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        try{
            UUID userId = extractUserId(request);
            System.out.println("User id in clear cart: " + userId);
            cartService.clearCart(userId);
        }catch(UserNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Cart cleared successfully",HttpStatus.OK);
    }

    @GetMapping("/get-items")
    public ResponseEntity<?> getCartItems() {
        UUID userID = extractUserId(request);
        return ResponseEntity.ok(cartService.fetchUserCart(userID));
    }

    private UUID extractUserId(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return jwtService.extractCustomerId(token);
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
