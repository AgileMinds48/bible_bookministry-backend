package com.evbooksministry.bibleandbookministry.controllers;

import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.dtos.BuyNow;
import com.evbooksministry.bibleandbookministry.dtos.PaymentResponse;
import com.evbooksministry.bibleandbookministry.enums.OrderStatus;
import com.evbooksministry.bibleandbookministry.exceptions.EmptyCart;
import com.evbooksministry.bibleandbookministry.exceptions.OrderNotFound;
import com.evbooksministry.bibleandbookministry.exceptions.UserNotFoundException;
import com.evbooksministry.bibleandbookministry.models.OrderItem;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.evbooksministry.bibleandbookministry.services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
    private final OrderService orderService;
    private final HttpServletRequest request;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService,
                           HttpServletRequest request,
                           JWTService jwtService,
                           UserRepository userRepository) {
        this.orderService = orderService;
        this.request = request;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout() {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        UUID userId = jwtService.extractCustomerId(token);
        System.out.println("userId from checkout: " + userId);

        PaymentResponse response;
        try {
            response = orderService.checkout(userId);
        }catch (EmptyCart e){
            throw new EmptyCart();
        }catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/customer/get-order")
    public ResponseEntity<?> getCustomerOrder() {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        UUID userID = jwtService.extractCustomerId(token);
        Set<OrderItem> orders = orderService.getBuyerOrder(userID);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/buy-now")
    public ResponseEntity<?> buyNow(@RequestBody BuyNow buyNow) {
        try{
            String header = request.getHeader("Authorization");
            String token = header.substring(7);
            UUID userID = jwtService.extractCustomerId(token);
            BuyNow finalBuyNow = buyNow.newUserId(userID);
            System.out.println("buy now request: " + finalBuyNow);
            return new ResponseEntity<>(orderService.buyNow(finalBuyNow), HttpStatus.OK);
        }catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    @GetMapping("/get-order/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable UUID orderId) {
        try{
            return new ResponseEntity<>(orderService.getOrderById(orderId), HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/callback")
    public String callback() {
        return "Agile minds";
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestParam UUID orderId, @RequestParam OrderStatus status) {
        try{
            return new ResponseEntity<>(orderService.updateOrderStatus(orderId, status), HttpStatus.OK);
        }catch (OrderNotFound e){
            throw new OrderNotFound();
        }
    }
}
