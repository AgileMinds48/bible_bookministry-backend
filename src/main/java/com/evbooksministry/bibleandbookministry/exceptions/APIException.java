package com.evbooksministry.bibleandbookministry.exceptions;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

public record APIException(String status, int statusCode,
                           com.evbooksministry.bibleandbookministry.exceptions.APIException.ApiError error,
                           String requestId) {

    public record ApiError(HttpStatus code, String details, Timestamp timestamp, String apiPath) {
    }
}
