package com.evbooksministry.bibleandbookministry.dtos;

public record ChangePasswordRequest (
        String oldPassword,
        String newPassword
){
}
