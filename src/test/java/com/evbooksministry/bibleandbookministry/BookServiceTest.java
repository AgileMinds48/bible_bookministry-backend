package com.evbooksministry.bibleandbookministry;

import com.evbooksministry.bibleandbookministry.dtos.AddBookRequest;
import com.evbooksministry.bibleandbookministry.dtos.BookDTO;
import com.evbooksministry.bibleandbookministry.dtos.UpdateBook;
import com.evbooksministry.bibleandbookministry.dtos.UpdateBookMedia;
import com.evbooksministry.bibleandbookministry.enums.BookCategory;
import com.evbooksministry.bibleandbookministry.mappers.BookMapper;
import com.evbooksministry.bibleandbookministry.models.Book;
import com.evbooksministry.bibleandbookministry.repositories.BookRepository;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.evbooksministry.bibleandbookministry.services.BookService;
import com.evbooksministry.bibleandbookministry.services.CloudinaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link BookService} class.
 * <p>
 * This class validates the core business logic of BookService, including CRUD operations and media handling.
 * All dependencies are mocked using Mockito to ensure isolated and reliable tests.
 * </p>
 */
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CloudinaryService cloudinaryService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    /**
     * Initializes mocks and the BookService instance before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(bookRepository, cloudinaryService, objectMapper, userRepository, bookMapper);
    }

    /**
     * Tests retrieval of all available books with pagination.
     * Verifies that the repository is called and the result is as expected.
     */
    @Test
    void testGetAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        BookDTO bookDTO = mock(BookDTO.class);
        Page<BookDTO> bookDTOPage = new PageImpl<>(Collections.singletonList(bookDTO));

        when(bookRepository.getAllByAvailable(pageable)).thenReturn(bookDTOPage);

        Page<BookDTO> result = bookService.getAllBooks(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository, times(1)).getAllByAvailable(pageable);
    }

    /**
     * Tests retrieval of a book by its ID when the book exists.
     * Verifies that the repository and mapper are called and the correct DTO is returned.
     */
    @Test
    void testGetBookById_found() {
        UUID bookId = UUID.randomUUID();
        Book book = mock(Book.class);
        BookDTO bookDTO = mock(BookDTO.class);

        when(bookRepository.findByBookId(bookId)).thenReturn(java.util.Optional.of(book));
        when(bookMapper.bookEntityToBookDTO(book)).thenReturn(bookDTO);

        var result = bookService.getBookById(bookId);

        assertTrue(result.isPresent());
        assertEquals(bookDTO, result.get());
        verify(bookRepository, times(1)).findByBookId(bookId);
        verify(bookMapper, times(1)).bookEntityToBookDTO(book);
    }

    /**
     * Tests retrieval of a book by its ID when the book does not exist.
     * Verifies that the repository is called and the result is empty.
     */
    @Test
    void testGetBookById_notFound() {
        UUID bookId = UUID.randomUUID();
        when(bookRepository.findByBookId(bookId)).thenReturn(java.util.Optional.empty());

        var result = bookService.getBookById(bookId);

        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findByBookId(bookId);
        verify(bookMapper, never()).bookEntityToBookDTO(any());
    }

    /**
     * Tests the addition of a new book, including media upload.
     * Verifies that the repository, cloud service, and mapper are called as expected.
     */
    @Test
    void testAddNewBook() throws Exception {
        AddBookRequest request = new AddBookRequest(
                "Test Book",
                "A test book description",
                new java.math.BigDecimal("19.99"),
                10,
                new java.math.BigDecimal("199.90"),
                10,
                BookCategory.RELIGIOUS
        );

        org.springframework.web.multipart.MultipartFile file1 = mock(org.springframework.web.multipart.MultipartFile.class);
        org.springframework.web.multipart.MultipartFile[] files = new org.springframework.web.multipart.MultipartFile[] { file1 };

        // Mock CloudinaryService to return a URL for the uploaded file
        when(cloudinaryService.uploadFile(file1)).thenReturn("http://cloudinary.com/test.jpg");

        Book savedBook = Book.builder()
                .bookTitle(request.bookTitle())
                .bookDescription(request.bookDescription())
                .bookPrice(request.bookPrice())
                .quantity(request.quantity())
                .amountInStock(request.amountInStock())
                .isAvailable(true)
                .media(java.util.List.of("http://cloudinary.com/test.jpg"))
                .bookCategory(request.bookCategory())
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookDTO bookDTO = mock(BookDTO.class);
        when(bookMapper.bookEntityToBookDTO(any(Book.class))).thenReturn(bookDTO);

        BookDTO result = bookService.addNewBook(request, files);

        assertNotNull(result);
        verify(cloudinaryService, times(1)).uploadFile(file1);
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookMapper, times(1)).bookEntityToBookDTO(any(Book.class));
    }

    /**
     * Tests updating an existing book's details.
     * Verifies that the repository is called, the entity is updated, and saved.
     */
    @Test
    void testUpdateBook() {
        UUID bookId = UUID.randomUUID();
        BookDTO bookDTO = new BookDTO(
                bookId,
                "Updated Title",
                "Updated Description",
                new java.math.BigDecimal("29.99"),
                5,
                new java.math.BigDecimal("149.95"),
                null,
                null,
                null,
                BookCategory.RELIGIOUS,
                20
        );

        Book bookEntity = new Book();
        bookEntity.setBookId(bookId);
        bookEntity.setBookTitle("Old Title");
        bookEntity.setBookDescription("Old Description");
        bookEntity.setBookPrice(new java.math.BigDecimal("10.00"));
        bookEntity.setQuantity(1);
        bookEntity.setBookValue(new java.math.BigDecimal("10.00"));

        when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.of(bookEntity));
        when(bookRepository.save(any(Book.class))).thenReturn(bookEntity);

        bookService.updateBook(bookDTO);

        assertEquals("Updated Title", bookEntity.getBookTitle());
        assertEquals("Updated Description", bookEntity.getBookDescription());
        assertEquals(new java.math.BigDecimal("29.99"), bookEntity.getBookPrice());
        assertEquals(new java.math.BigDecimal("149.95"), bookEntity.getBookValue());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(bookEntity);
    }

    /**
     * Tests updating the media of a product.
     * Verifies that the repository, cloud service, and mapper are called and the media list is updated.
     */
    @Test
    void testUpdateProductMedia() throws Exception {
        UUID bookId = UUID.randomUUID();
        org.springframework.web.multipart.MultipartFile file1 = mock(org.springframework.web.multipart.MultipartFile.class);
        org.springframework.web.multipart.MultipartFile[] files = new org.springframework.web.multipart.MultipartFile[] { file1 };
        String uploadedUrl = "http://cloudinary.com/media1.jpg";

        Book bookEntity = new Book();
        bookEntity.setBookId(bookId);
        bookEntity.setMedia(new java.util.ArrayList<>());

        when(bookRepository.findByBookId(bookId)).thenReturn(java.util.Optional.of(bookEntity));
        when(cloudinaryService.uploadFile(file1)).thenReturn(uploadedUrl);
        when(bookRepository.save(any(Book.class))).thenReturn(bookEntity);
        BookDTO bookDTO = mock(BookDTO.class);
        when(bookMapper.bookEntityToBookDTO(bookEntity)).thenReturn(bookDTO);

        UpdateBookMedia update = new UpdateBookMedia(bookId, files);
        BookDTO result = bookService.updateProductMedia(update);

        assertNotNull(result);
        assertEquals(1, bookEntity.getMedia().size());
        assertEquals(uploadedUrl, bookEntity.getMedia().get(0));
        verify(bookRepository, times(1)).findByBookId(bookId);
        verify(cloudinaryService, times(1)).uploadFile(file1);
        verify(bookRepository, times(1)).save(bookEntity);
        verify(bookMapper, times(1)).bookEntityToBookDTO(bookEntity);
    }

    /**
     * Tests updating all product details, including media.
     * Verifies that the repository, cloud service, and mapper are called and all fields are updated.
     */
    @Test
    void testUpdateProduct() throws Exception {
        UUID bookId = UUID.randomUUID();
        org.springframework.web.multipart.MultipartFile file1 = mock(org.springframework.web.multipart.MultipartFile.class);
        org.springframework.web.multipart.MultipartFile[] files = new org.springframework.web.multipart.MultipartFile[] { file1 };
        String uploadedUrl = "http://cloudinary.com/media1.jpg";

        UpdateBook updateBook = new UpdateBook(
                bookId,
                BookCategory.RELIGIOUS,
                "Updated Description",
                "Updated Title",
                new java.math.BigDecimal("39.99"),
                15,
                null,
                7
        );

        Book bookEntity = new Book();
        bookEntity.setBookId(bookId);
        bookEntity.setBookTitle("Old Title");
        bookEntity.setBookDescription("Old Description");
        bookEntity.setBookPrice(new java.math.BigDecimal("10.00"));
        bookEntity.setQuantity(1);
        bookEntity.setBookValue(new java.math.BigDecimal("10.00"));
        bookEntity.setAmountInStock(5);
        bookEntity.setBookCategory(BookCategory.RELIGIOUS);
        bookEntity.setMedia(new java.util.ArrayList<>());

        when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.of(bookEntity));
        when(cloudinaryService.uploadFile(file1)).thenReturn(uploadedUrl);
        when(bookRepository.save(any(Book.class))).thenReturn(bookEntity);
        BookDTO bookDTO = mock(BookDTO.class);
        when(bookMapper.bookEntityToBookDTO(bookEntity)).thenReturn(bookDTO);

        BookDTO result = bookService.updateProduct(updateBook, files);

        assertNotNull(result);
        assertEquals("Updated Title", bookEntity.getBookTitle());
        assertEquals("Updated Description", bookEntity.getBookDescription());
        assertEquals(new java.math.BigDecimal("39.99"), bookEntity.getBookPrice());
        assertEquals(BookCategory.RELIGIOUS, bookEntity.getBookCategory());
        assertEquals(7, bookEntity.getQuantity());
        assertEquals(15, bookEntity.getAmountInStock());
        assertEquals(1, bookEntity.getMedia().size());
        assertEquals(uploadedUrl, bookEntity.getMedia().get(0));
        verify(bookRepository, times(1)).findById(bookId);
        verify(cloudinaryService, times(1)).uploadFile(file1);
        verify(bookRepository, times(1)).save(bookEntity);
        verify(bookMapper, times(1)).bookEntityToBookDTO(bookEntity);
    }

    /**
     * Tests the private {@code updateProductMedia(MultipartFile[])} method of the {@link BookService} class.
     * <p>
     * This test uses Java reflection to access the private method, mocks the behavior of the {@link CloudinaryService}
     * to simulate file uploads, and verifies that the method returns a list of URLs corresponding to the uploaded files.
     * The test also ensures that the upload operation is invoked for each file in the input array.
     * </p>
     *
     * @throws Exception if reflection or invocation fails
     */
    @Test
    void testUpdateProductMediaList() throws Exception {
        // Arrange: Create mock MultipartFile objects and expected URLs
        org.springframework.web.multipart.MultipartFile file1 = mock(org.springframework.web.multipart.MultipartFile.class);
        org.springframework.web.multipart.MultipartFile file2 = mock(org.springframework.web.multipart.MultipartFile.class);
        org.springframework.web.multipart.MultipartFile[] files = new org.springframework.web.multipart.MultipartFile[] { file1, file2 };
        String url1 = "http://cloudinary.com/media1.jpg";
        String url2 = "http://cloudinary.com/media2.jpg";

        // Mock CloudinaryService to return predefined URLs for each file
        when(cloudinaryService.uploadFile(file1)).thenReturn(url1);
        when(cloudinaryService.uploadFile(file2)).thenReturn(url2);

        // Act: Use reflection to invoke the private updateProductMedia method
        java.lang.reflect.Method method = BookService.class.getDeclaredMethod("updateProductMedia", org.springframework.web.multipart.MultipartFile[].class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.List<String> result = (java.util.List<String>) method.invoke(bookService, (Object) files);

        // Assert: Validate the returned list and verify interactions
        assertNotNull(result, "The result list should not be null");
        assertEquals(2, result.size(), "The result list should contain two URLs");
        assertEquals(url1, result.get(0), "The first URL should match the expected value");
        assertEquals(url2, result.get(1), "The second URL should match the expected value");
        verify(cloudinaryService, times(1)).uploadFile(file1);
        verify(cloudinaryService, times(1)).uploadFile(file2);
    }
} 