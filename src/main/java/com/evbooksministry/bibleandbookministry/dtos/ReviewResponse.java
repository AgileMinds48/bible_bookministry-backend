package com.evbooksministry.bibleandbookministry.dtos;

import java.sql.Timestamp;

public class ReviewResponse {
    private String userName;
    private int rating;
    private String comment;
    private Timestamp createdAt;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
} 