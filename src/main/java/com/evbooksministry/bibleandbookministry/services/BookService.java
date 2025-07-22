package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.dtos.*;
import com.evbooksministry.bibleandbookministry.enums.BookCategory;
import com.evbooksministry.bibleandbookministry.exceptions.BookNotFound;
import com.evbooksministry.bibleandbookministry.exceptions.InvalidDetails;
import com.evbooksministry.bibleandbookministry.mappers.BookMapper;
import com.evbooksministry.bibleandbookministry.models.Book;
import com.evbooksministry.bibleandbookministry.repositories.BookRepository;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final CloudinaryService cloudinaryService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository,
                       CloudinaryService cloudinaryService,
                       ObjectMapper objectMapper,
                       UserRepository userRepository,
                       BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.cloudinaryService = cloudinaryService;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.bookMapper = bookMapper;
    }


    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookRepository.getAllByAvailable(pageable);
    }

    public Optional<BookDTO> getBookById(UUID bookId) {
        return bookRepository
                .findByBookId(bookId)
                .map(bookMapper::bookEntityToBookDTO);
    }

    public BookDTO addNewBook(AddBookRequest request, MultipartFile[] bookImages
    ) throws IOException {
        return addBook(request, bookRepository, bookImages);
    }


    public void updateBook(BookDTO book){
        Book bookToUpdate = bookRepository.findById(book.bookId())
                .orElseThrow(() -> new BookNotFound("Book not found"));
        if(bookToUpdate != null) {
            bookToUpdate.setBookTitle(book.bookTitle());
            bookToUpdate.setBookDescription(book.bookDescription());
            bookToUpdate.setBookPrice(book.bookPrice());
            bookToUpdate.setBookValue(bookToUpdate.getBookPrice().multiply(BigDecimal.valueOf(book.quantity())));
            bookRepository.save(bookToUpdate);
        }
    }

    public void deleteProduct(UUID productId) {
        Optional<Book> bookToDelete = bookRepository.findById(productId);
        if (bookToDelete.isPresent()) {
            Book book = bookToDelete.get();
            book.setAvailable(false);
            bookRepository.save(book);
        }else{
            throw new BookNotFound();
        }
    }

    public Page<BookDTO> getProductsByCategory(String category, Pageable pageable) {
        BookCategory categoryEnum = BookCategory.valueOf(category.toUpperCase());
        System.out.println(categoryEnum);
        return bookRepository
                .findByBookCategory(categoryEnum, pageable)
                .map(bookMapper::bookEntityToBookDTO);
    }

    private BookDTO addBook(AddBookRequest request,
                                  BookRepository bookRepository, MultipartFile[] bookFiles) throws IOException {
        List<String> bookMedia = new ArrayList<>();

        //todo validate images,
        for (MultipartFile file : bookFiles) {
            String prodFile = cloudinaryService.uploadFile(file);
            bookMedia.add(prodFile);
        }



        Book book = Book.builder()
                .bookTitle(request.bookTitle())
                .bookDescription(request.bookDescription())
                .bookPrice(request.bookPrice())
                .quantity(request.quantity())
                .amountInStock(request.amountInStock())
                .isAvailable(true)
                .createdOn(Timestamp.from(Instant.now()))
                .updatedOn(Timestamp.from(Instant.now()))
                .media(bookMedia)
                .bookCategory(request.bookCategory())
                .build();
        bookRepository.save(book);

        return bookMapper.bookEntityToBookDTO(book);
    }

    public BookDTO updateBookDetails(UpdateBookDetails request) throws InvalidDetails {
        UUID bookId = request.bookId();

        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(BookNotFound::new);

        switch (request.field().toLowerCase().trim()) {
            case "bookTitle"-> {
                book.setBookTitle(request.value().toString());
                bookRepository.save(book);
            }

            case "bookdescription"-> {
                book.setBookDescription(request.value().toString());
                bookRepository.save(book);
            }

            case "bookprice"-> {
                book.setBookPrice((BigDecimal.valueOf(Double.parseDouble(request.value().toString()))));
                bookRepository.save(book);
            }

            case "category"-> {
                book.setBookCategory(objectMapper.convertValue(request.value().toString(), BookCategory.class));
                bookRepository.save(book);
            }

            default-> throw new InvalidDetails("Invalid entry");
        }
        return bookMapper.bookEntityToBookDTO(book);
    }

    public BookDTO updateProductMedia(UpdateBookMedia update) throws IOException {
        Book book = bookRepository.findByBookId(update.bookId())
                .orElseThrow(() -> new BookNotFound("Product not found"));
        MultipartFile[] media = update.bookMedia();
        List<String> bookMedia = new ArrayList<>();
        for (MultipartFile file : media) {
            String prodFile = cloudinaryService.uploadFile(file);
            bookMedia.add(prodFile);
        }
        book.setMedia(bookMedia);
        bookRepository.save(book);
        return bookMapper.bookEntityToBookDTO(book);
    }

    public BookDTO updateProduct(UpdateBook request, MultipartFile[] bookFiles) throws IOException {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(BookNotFound::new);
        book.setBookTitle(request.bookTitle());
        book.setBookDescription(request.bookDescription());
        book.setBookPrice(request.bookPrice());
        book.setBookCategory(request.bookCategory());
        book.setQuantity(request.bookQuantity());
        book.setAvailable(true);
        book.setAmountInStock(request.amountInStock());
        List<String> bookMedia = updateProductMedia(bookFiles);
        book.setMedia(bookMedia);
        bookRepository.save(book);
        return bookMapper.bookEntityToBookDTO(book);
    }

    private List<String> updateProductMedia(MultipartFile [] files) throws IOException {
        List<String> newProductMedia = new ArrayList<>();
        for (MultipartFile file : files) {
            String prodFile = cloudinaryService.uploadFile(file);
            newProductMedia.add(prodFile);
        }
        return newProductMedia;
    }
}
