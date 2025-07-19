package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.dtos.ReviewRequest;
import com.evbooksministry.bibleandbookministry.dtos.ReviewResponse;
import com.evbooksministry.bibleandbookministry.models.Book;
import com.evbooksministry.bibleandbookministry.models.Review;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.BookRepository;
import com.evbooksministry.bibleandbookministry.repositories.ReviewRepository;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public ReviewResponse addReview(ReviewRequest request) {
        Optional<Book> bookOpt = bookRepository.findById(request.getBookId());
        Optional<Users> userOpt = userRepository.findById(request.getUserId());
        if (bookOpt.isEmpty() || userOpt.isEmpty()) {
            throw new IllegalArgumentException("Book or User not found");
        }
        Review review = new Review();
        review.setBook(bookOpt.get());
        review.setUser(userOpt.get());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        Review saved = reviewRepository.save(review);
        return mapToResponse(saved);
    }

    public List<ReviewResponse> getReviewsForBook(UUID bookId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book not found");
        }
        List<Review> reviews = reviewRepository.findByBook(bookOpt.get());
        return reviews.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private ReviewResponse mapToResponse(Review review) {
        ReviewResponse resp = new ReviewResponse();
        resp.setUserName(review.getUser().getUserName());
        resp.setRating(review.getRating());
        resp.setComment(review.getComment());
        resp.setCreatedAt(review.getCreatedAt());
        return resp;
    }
} 