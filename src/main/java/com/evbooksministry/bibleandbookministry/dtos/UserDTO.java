package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.enums.Gender;

import java.sql.Timestamp;
import java.util.UUID;

public record UserDTO (
        UUID userId,
        String firstName,
        String lastName,
        String userName,
        Gender userGender,
        String password,
        String email,
        String phoneNumber,

        String city,
        String country,
        String state,
        Timestamp createdAt,
        Timestamp updatedAt,
        String profilePictureURL
) {
    public UserDTO newProfilePictureURL(String url){
        return new UserDTO(
                this.userId,
                this.firstName,
                this.lastName,
                this.userName,
                this.userGender,
                this.password,
                this.email,
                this.phoneNumber,

                this.city,
                this.country,
                this.state,
                this.createdAt,
                this.updatedAt,
                url
        );
    }
}
