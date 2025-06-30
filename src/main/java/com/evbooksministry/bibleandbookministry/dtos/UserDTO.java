package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.enums.Gender;
import com.evbooksministry.bibleandbookministry.enums.UserRole;

import java.sql.Timestamp;

public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private Gender userGender;
    private String password;
    private String email;
    private String phoneNumber;
    private UserRole userRole;
    private String city;
    private String country;
    private String state;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String profilePictureURL;

    public UserDTO(Long userId,
                   String firstName,
                   String lastName,
                   String userName,
                   Gender userGender,
                   String password,
                   String email,
                   String phoneNumber,
                   UserRole userRole,
                   String city,
                   String country,
                   String state,
                   Timestamp createdAt,
                   Timestamp updatedAt,
                   String profilePictureURL) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.userGender = userGender;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
        this.city = city;
        this.country = country;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.profilePictureURL = profilePictureURL;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Gender getUserGender() {
        return userGender;
    }

    public void setUserGender(Gender userGender) {
        this.userGender = userGender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
