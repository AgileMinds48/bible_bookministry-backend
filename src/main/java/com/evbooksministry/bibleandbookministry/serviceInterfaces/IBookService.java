package com.evbooksministry.bibleandbookministry.serviceInterfaces;

import com.evbooksministry.bibleandbookministry.dtos.*;
import com.evbooksministry.bibleandbookministry.exceptions.InvalidDetails;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface IBookService {
    Page<BookDTO> getAllBooks(Pageable pageable);
    Optional<BookDTO> getBookById(UUID bookId);
    BookDTO addNewBook(AddBookRequest request, MultipartFile[] bookImages, Users user) throws IOException;
    void updateBook(BookDTO book);
    void deleteProduct(UUID productId);
    Page<BookDTO> getProductsByCategory(String category, Pageable pageable);
    BookDTO addBook(AddBookRequest request, BookRepository bookRepository, MultipartFile[] bookFiles, Users user) throws IOException;
    BookDTO updateBookDetails(UpdateBookDetails request) throws InvalidDetails;
    BookDTO updateProductMedia(UpdateBookMedia update) throws IOException;
    BookDTO updateProduct(UpdateBook request, MultipartFile[] bookFiles) throws IOException;

}
