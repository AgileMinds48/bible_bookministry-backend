package com.evbooksministry.bibleandbookministry.config;


import com.evbooksministry.bibleandbookministry.exceptions.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;

@ControllerAdvice
public class APIExceptionHandler {
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<?> handleUserNotFoundException(HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.NOT_FOUND.value(),
                new APIException.ApiError(
                        HttpStatus.NOT_FOUND,
                        "Invalid user",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {EmptyCart.class})
    public ResponseEntity<?> handleEmptyCartException(HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.ApiError(
                        HttpStatus.NOT_FOUND,
                        "User's cart is empty",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredSessionException.class)
    public ResponseEntity<?> handleExpiredSessionException(HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.ApiError(
                        HttpStatus.BAD_REQUEST,
                        "User's session has expired. Please login again",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<?> handleProductNotFoundException(HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.NOT_FOUND.value(),
                new APIException.ApiError(
                        HttpStatus.NOT_FOUND,
                        "Product requested does not exist",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {JsonProcessingException.class})
    public ResponseEntity<?> handleJsonProcessingException(HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new APIException.ApiError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An error occurred on the server. Try again later",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.CONTINUE.value(),
                new APIException.ApiError(
                        HttpStatus.CONFLICT,
                        "User with this email already exists",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<?> handleBadCredentialsException(HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.UNAUTHORIZED.value(),
                new APIException.ApiError(
                        HttpStatus.UNAUTHORIZED,
                        "User entered wrong credentials",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }
}
