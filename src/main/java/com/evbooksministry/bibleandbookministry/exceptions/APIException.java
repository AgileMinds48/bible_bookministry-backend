package com.evbooksministry.bibleandbookministry.exceptions;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

public class APIException {
    private final String status;
    private final int statusCode;
    private final ApiError error;
    private final String requestId;

    public APIException(String status, int statusCode, ApiError error, String requestId) {
        this.status = status;
        this.statusCode = statusCode;
        this.error = error;
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ApiError getError() {
        return error;
    }

    public String getRequestId() {
        return requestId;
    }

    public static class ApiError{
        private final HttpStatus code;
        private final String details;
        private final Timestamp timestamp;
        private final String apiPath;

        public ApiError(HttpStatus code,
                        String details,
                        Timestamp timestamp,
                        String apiPath) {
            this.code = code;
            this.details = details;
            this.timestamp = timestamp;
            this.apiPath = apiPath;
        }

        public HttpStatus getCode() {
            return code;
        }

        public String getDetails() {
            return details;
        }

        public Timestamp getTimestamp() {
            return timestamp;
        }

        public String getApiPath() {
            return apiPath;
        }
    }
}
