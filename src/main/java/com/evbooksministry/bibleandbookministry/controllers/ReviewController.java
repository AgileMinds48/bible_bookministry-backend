package com.evbooksministry.bibleandbookministry.controllers;

import com.evbooksministry.bibleandbookministry.dtos.ReviewRequest;
import com.evbooksministry.bibleandbookministry.dtos.ReviewResponse;
import com.evbooksministry.bibleandbookministry.services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(@RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.addReview(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsForBook(@PathVariable UUID bookId) {
        List<ReviewResponse> reviews = reviewService.getReviewsForBook(bookId);
        return ResponseEntity.ok(reviews);
    }
} 