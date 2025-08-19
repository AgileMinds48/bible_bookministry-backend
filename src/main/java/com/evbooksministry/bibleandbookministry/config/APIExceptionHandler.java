package com.evbooksministry.bibleandbookministry.config;


import com.evbooksministry.bibleandbookministry.exceptions.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
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

    @ExceptionHandler(value = {SQLException.class})
    public ResponseEntity<?> handleSchemaViolation(SQLException ex, HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.CONFLICT.value(),
                new APIException.ApiError(
                        HttpStatus.CONFLICT,
                        "User attempted to enter a duplicate value",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<?> handleWrongRequestMethod(HttpRequestMethodNotSupportedException ex, HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.ApiError(
                        HttpStatus.BAD_REQUEST,
                        "Client is using wrong request method",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST  );
    }

    @ExceptionHandler(value = {EmployeeNotFound.class})
    public ResponseEntity<?> handleEmployeeNotFoun(EmployeeNotFound ex, HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.NOT_FOUND.value(),
                new APIException.ApiError(
                        HttpStatus.NOT_FOUND,
                        "Employee not found",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND  );
    }

    @ExceptionHandler(value = {EmptyCart.class})
    public ResponseEntity<?> handleEmptyCartException(EmptyCart ex, HttpServletRequest request){
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
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredSessionException.class)
    public ResponseEntity<?> handleExpiredSessionException(ExpiredSessionException ex, HttpServletRequest request){
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
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<?> handleInvalidFormatException(InvalidFormatException ex, HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.ApiError(
                        HttpStatus.BAD_REQUEST,
                        "Client sent wrong category",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestURI()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookNotFound.class)
    public ResponseEntity<?> handleBookNotFoundException(BookNotFound ex, HttpServletRequest request){
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
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {JsonProcessingException.class})
    public ResponseEntity<?> handleJsonProcessingException(JsonProcessingException ex, HttpServletRequest request){
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
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExists ex, HttpServletRequest request){
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
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.UNAUTHORIZED.value(),
                new APIException.ApiError(
                        HttpStatus.UNAUTHORIZED,
                        "You entered wrong credentials",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {OrderNotFound.class})
    public ResponseEntity<?> handleOrderNotFoundException(OrderNotFound ex, HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.NOT_FOUND.value(),
                new APIException.ApiError(
                        HttpStatus.NOT_FOUND,
                        "Order not found",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UnauthorizedAction.class})
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedAction ex, HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.UNAUTHORIZED.value(),
                new APIException.ApiError(
                        HttpStatus.UNAUTHORIZED,
                        "A customer cannot add a new book",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {InvalidEmail.class})
    public ResponseEntity<?> handleInvalidEmail(InvalidEmail ex, HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.ApiError(
                        HttpStatus.BAD_REQUEST,
                        "User email is invalid",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryDoesNotExist.class)
    public ResponseEntity<?> handleCategoryDoesNotExist(CategoryDoesNotExist ex, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.ApiError(
                        HttpStatus.BAD_REQUEST,
                        "Category does not exist",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestURI()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryAlreadyExists.class)
    public ResponseEntity<?> handleCategoryAlreadyExists(CategoryAlreadyExists ex, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid",
                HttpStatus.CONFLICT.value(),
                new APIException.ApiError(
                        HttpStatus.CONFLICT,
                        "The category the user attempted to create already exists",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestURI()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(ex);
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

}
