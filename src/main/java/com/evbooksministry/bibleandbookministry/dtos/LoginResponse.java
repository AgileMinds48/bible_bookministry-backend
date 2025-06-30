package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.enums.UserRole;

public class LoginResponse {
    private boolean success;
    private UserRole userRole;


    public LoginResponse(boolean success, UserRole userRole) {
        this.success = success;
        this.userRole = userRole;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
