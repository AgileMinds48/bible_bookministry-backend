package com.evbooksministry.bibleandbookministry;

import com.evbooksministry.bibleandbookministry.dtos.AddOrRemoveFromCartRequest;
import com.evbooksministry.bibleandbookministry.exceptions.BookNotFound;
import com.evbooksministry.bibleandbookministry.exceptions.EmptyCart;
import com.evbooksministry.bibleandbookministry.exceptions.UserNotFoundException;
import com.evbooksministry.bibleandbookministry.models.Book;
import com.evbooksministry.bibleandbookministry.models.Cart;
import com.evbooksministry.bibleandbookministry.models.CartItems;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.BookRepository;
import com.evbooksministry.bibleandbookministry.repositories.CartItemsRepository;
import com.evbooksministry.bibleandbookministry.repositories.CartRepository;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import com.evbooksministry.bibleandbookministry.services.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemsRepository cartItemsRepository;

    @InjectMocks
    private CartService cartService;

    /**
     * Initializes mocks and the CartService instance before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartService = new CartService(userRepository, bookRepository, cartRepository, cartItemsRepository);
    }

    /**
     * Tests adding a new item to a new cart for a user.
     * Verifies that the cart, user, and book are saved and the result is not null.
     */
    @Test
    void testAddItemToCart_NewCartAndItem() {
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        AddOrRemoveFromCartRequest request = new AddOrRemoveFromCartRequest(bookId, 2);
        Users user = mock(Users.class);
        Book book = mock(Book.class);
        Cart cart = new Cart();
        cart.setCartItems(new HashSet<>());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getUserCart()).thenReturn(null);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(book.isAvailable()).thenReturn(true);
        when(book.getAmountInStock()).thenReturn(10);
        when(book.getBookPrice()).thenReturn(new BigDecimal("20.00"));
        when(user.getUserCart()).thenReturn(cart);
        Set<CartItems> result = cartService.addItemToCart(request, userId);
        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(userRepository, times(1)).save(any(Users.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    /**
     * Tests adding an item to the cart when the item already exists.
     * Verifies that the quantity and price are updated accordingly.
     */
    @Test
    void testAddItemToCart_ExistingItem() {
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        AddOrRemoveFromCartRequest request = new AddOrRemoveFromCartRequest(bookId, 1);
        Users user = mock(Users.class);
        Book book = mock(Book.class);
        Cart cart = new Cart();
        CartItems item = new CartItems();
        item.setBook(book);
        item.setQuantity(1);
        item.setPrice(new BigDecimal("20.00"));
        Set<CartItems> items = new HashSet<>();
        items.add(item);
        cart.setCartItems(items);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getUserCart()).thenReturn(cart);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(book.isAvailable()).thenReturn(true);
        when(book.getAmountInStock()).thenReturn(10);
        when(book.getBookPrice()).thenReturn(new BigDecimal("20.00"));
        when(book.getBookId()).thenReturn(bookId);
        Set<CartItems> result = cartService.addItemToCart(request, userId);
        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(userRepository, times(1)).save(any(Users.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    /**
     * Tests adding an item to the cart when the book is not found.
     * Expects a BookNotFound exception to be thrown.
     */
    @Test
    void testAddItemToCart_BookNotFound() {
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        AddOrRemoveFromCartRequest request = new AddOrRemoveFromCartRequest(bookId, 1);
        Users user = mock(Users.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getUserCart()).thenReturn(new Cart());
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        assertThrows(BookNotFound.class, () -> cartService.addItemToCart(request, userId));
    }

    /**
     * Tests adding an item to the cart when the user is not found.
     * Expects a UserNotFoundException to be thrown.
     */
    @Test
    void testAddItemToCart_UserNotFound() {
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        AddOrRemoveFromCartRequest request = new AddOrRemoveFromCartRequest(bookId, 1);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> cartService.addItemToCart(request, userId));
    }

    /**
     * Tests removing an item from the cart.
     * Verifies that the item is removed and the cart is saved.
     */
    @Test
    void testRemoveItemFromCart() {
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        AddOrRemoveFromCartRequest request = new AddOrRemoveFromCartRequest(bookId, 1);
        Users user = mock(Users.class);
        Cart cart = new Cart();
        CartItems item = new CartItems();
        Book book = mock(Book.class);
        item.setBook(book);
        when(book.getBookId()).thenReturn(bookId);
        Set<CartItems> items = new HashSet<>();
        items.add(item);
        cart.setCartItems(items);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getUserCart()).thenReturn(cart);
        cartService.removeItemFromCart(request, userId);
        assertFalse(cart.getCartItems().contains(item));
        verify(cartRepository, times(1)).save(cart);
    }

    /**
     * Tests clearing all items from the cart.
     * Verifies that the cart is empty and saved.
     */
    @Test
    void testClearCart() {
        UUID userId = UUID.randomUUID();
        Users user = mock(Users.class);
        Cart cart = new Cart();
        CartItems item = new CartItems();
        Set<CartItems> items = new HashSet<>();
        items.add(item);
        cart.setCartItems(items);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getUserCart()).thenReturn(cart);
        cartService.clearCart(userId);
        assertTrue(cart.getCartItems().isEmpty());
        verify(cartRepository, times(1)).save(cart);
    }

    /**
     * Tests fetching cart items when the cart is not empty.
     * Verifies that the correct number of items is returned.
     */
    @Test
    void testFetchCartItems_Success() {
        UUID userId = UUID.randomUUID();
        Users user = mock(Users.class);
        Cart cart = new Cart();
        CartItems item = new CartItems();
        Set<CartItems> items = new HashSet<>();
        items.add(item);
        cart.setCartItems(items);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getUserCart()).thenReturn(cart);
        Set<CartItems> result = cartService.fetchCartItems(userId);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * Tests fetching cart items when the cart is empty.
     * Expects an EmptyCart exception to be thrown.
     */
    @Test
    void testFetchCartItems_EmptyCart() {
        UUID userId = UUID.randomUUID();
        Users user = mock(Users.class);
        Cart cart = new Cart();
        cart.setCartItems(new HashSet<>());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getUserCart()).thenReturn(cart);
        assertThrows(EmptyCart.class, () -> cartService.fetchCartItems(userId));
    }

    /**
     * Tests fetching user cart items directly from the repository.
     * Verifies that the correct number of items is returned.
     */
    @Test
    void testFetchUserCartItems() {
        UUID userId = UUID.randomUUID();
        CartItems item = new CartItems();
        Set<CartItems> items = Collections.singleton(item);
        when(cartItemsRepository.findCartItemsByCart_Users_UserId(userId)).thenReturn(items);
        Set<CartItems> result = cartService.fetchUserCartItems(userId);
        assertNotNull(result);
        assertEquals(1, result.size());
    }
} 