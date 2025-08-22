package com.evbooksministry.bibleandbookministry.dtos;

import java.util.UUID;

public class ReviewRequest {
    private UUID bookId;
    private int rating;
    private String comment;
    // Optionally, include userId if not derived from authentication
    private UUID userId;

    public UUID getBookId() { return bookId; }
    public void setBookId(UUID bookId) { this.bookId = bookId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
} 