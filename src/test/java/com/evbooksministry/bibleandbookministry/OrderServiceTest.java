/*

package com.evbooksministry.bibleandbookministry;

import com.evbooksministry.bibleandbookministry.config.EmailService;
import com.evbooksministry.bibleandbookministry.dtos.BuyNow;
import com.evbooksministry.bibleandbookministry.dtos.PaymentResponse;
import com.evbooksministry.bibleandbookministry.enums.OrderStatus;
import com.evbooksministry.bibleandbookministry.exceptions.EmptyCart;
import com.evbooksministry.bibleandbookministry.exceptions.OrderNotFound;
import com.evbooksministry.bibleandbookministry.exceptions.UserNotFoundException;
import com.evbooksministry.bibleandbookministry.models.*;
import com.evbooksministry.bibleandbookministry.repositories.*;
import com.evbooksministry.bibleandbookministry.services.OrderService;
import com.evbooksministry.bibleandbookministry.services.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

*/
/**
 * Unit tests for OrderService covering checkout, buy now, order retrieval, status updates, and order items.
 * Each test uses Mockito to mock dependencies and is documented for clarity.
 *//*

class OrderServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private OrderService orderService;

    */
/**
     * Sets up mocks and the OrderService instance before each test.
     *//*

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(userRepository, orderRepository, cartRepository, emailService, paymentService, paymentRepository, orderItemRepository, bookRepository);
    }

    */
/**
     * Verifies successful checkout: order is created, payment processed, and response returned.
     *//*

    @Test
    void testCheckout_Success() throws JsonProcessingException {
        UUID userId = UUID.randomUUID();
        Users user = mock(Users.class);
        Cart cart = new Cart();
        CartItems cartItem = new CartItems();
        Book book = mock(Book.class);
        PaymentResponse.Data data = new PaymentResponse.Data("http://test.com", "code123", "ref123", new BigDecimal("40.00"));
        PaymentResponse paymentResponse = new PaymentResponse(true, "Success", data);
        Payment payment = new Payment();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getUserCart()).thenReturn(cart);
        when(user.getEmail()).thenReturn("test@example.com");
        when(book.getBookPrice()).thenReturn(new BigDecimal("20.00"));
        when(book.getAmountSold()).thenReturn(0);
        cartItem.setBook(book);
        cartItem.setQuantity(2);
        cartItem.setPrice(new BigDecimal("40.00"));
        Set<CartItems> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);
        doReturn(paymentResponse).when(paymentService).processPayment(anyString(), any(BigDecimal.class));
        when(orderRepository.save(any(CustomerOrders.class))).thenReturn(new CustomerOrders());
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponse result = orderService.checkout(userId);

        assertNotNull(result);
        verify(orderRepository, times(2)).save(any(CustomerOrders.class));
        verify(paymentService, times(1)).processPayment(anyString(), any(BigDecimal.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    */
/**
     * Verifies checkout throws UserNotFoundException if user is missing.
     *//*

    @Test
    void testCheckout_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> orderService.checkout(userId));
    }

    */
/**
     * Verifies checkout throws EmptyCart if cart is empty.
     *//*

    @Test
    void testCheckout_EmptyCart() {
        UUID userId = UUID.randomUUID();
        Users user = mock(Users.class);
        Cart cart = new Cart();
        cart.setCartItems(new HashSet<>());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getUserCart()).thenReturn(cart);
        assertThrows(EmptyCart.class, () -> orderService.checkout(userId));
    }

    */
/**
     * Verifies buyNow creates order and returns payment response.
     *//*

    @Test
    void testBuyNow_Success() throws JsonProcessingException {
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        BuyNow request = new BuyNow(userId, bookId, 2);
        Users user = mock(Users.class);
        Book book = mock(Book.class);
        PaymentResponse.Data data = new PaymentResponse.Data("http://test.com", "code123", "ref123", new BigDecimal("40.00"));
        PaymentResponse paymentResponse = new PaymentResponse(true, "Success", data);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn("test@example.com");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(book.getBookPrice()).thenReturn(new BigDecimal("20.00"));
        when(book.getAmountInStock()).thenReturn(10);
        when(book.getAmountSold()).thenReturn(0);
        doReturn(paymentResponse).when(paymentService).processPayment(anyString(), any(BigDecimal.class));
        when(orderRepository.save(any(CustomerOrders.class))).thenReturn(new CustomerOrders());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        PaymentResponse result = orderService.buyNow(request);

        assertNotNull(result);
        verify(orderRepository, times(1)).save(any(CustomerOrders.class));
        verify(paymentService, times(1)).processPayment(anyString(), any(BigDecimal.class));
        // verify(bookRepository, times(1)).save(any(Book.class));
    }

    */
/**
     * Verifies buyNow throws UserNotFoundException if user is missing.
     *//*

    @Test
    void testBuyNow_UserNotFound() {
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        BuyNow request = new BuyNow(userId, bookId, 1);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> orderService.buyNow(request));
    }

    */
/**
     * Verifies buyNow throws OrderNotFound if book is missing.
     *//*

    @Test
    void testBuyNow_BookNotFound() {
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        BuyNow request = new BuyNow(userId, bookId, 1);
        Users user = mock(Users.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFound.class, () -> orderService.buyNow(request));
    }

    */
/**
     * Verifies getOrderById returns the order if found.
     *//*

    @Test
    void testGetOrderById_Found() {
        UUID orderId = UUID.randomUUID();
        CustomerOrders order = new CustomerOrders();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Optional<CustomerOrders> result = orderService.getOrderById(orderId);
        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }

    */
/**
     * Verifies getOrderById returns empty if order not found.
     *//*

    @Test
    void testGetOrderById_NotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        Optional<CustomerOrders> result = orderService.getOrderById(orderId);
        assertTrue(result.isEmpty());
    }

    */
/**
     * Verifies getBuyerOrders returns all orders for a buyer.
     *//*

    @Test
    void testGetBuyerOrders() {
        UUID buyerId = UUID.randomUUID();
        CustomerOrders order1 = new CustomerOrders();
        CustomerOrders order2 = new CustomerOrders();
        Set<CustomerOrders> orders = new HashSet<>();
        orders.add(order1);
        orders.add(order2);
        when(orderRepository.findByUser_UserId(buyerId)).thenReturn(orders);
        Set<CustomerOrders> result = orderService.getBuyerOrders(buyerId);
        assertEquals(2, result.size());
        assertTrue(result.contains(order1));
        assertTrue(result.contains(order2));
    }

    */
/**
     * Verifies updateOrderStatus updates and saves the order status.
     *//*

    @Test
    void testUpdateOrderStatus() {
        UUID orderId = UUID.randomUUID();
        CustomerOrders order = new CustomerOrders();
        order.setOrderStatus(OrderStatus.PENDING);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(CustomerOrders.class))).thenReturn(order);
        CustomerOrders result = orderService.updateOrderStatus(orderId, OrderStatus.PAID);
        assertEquals(OrderStatus.PAID, result.getOrderStatus());
        verify(orderRepository, times(1)).save(order);
    }

    */
/**
     * Verifies updateOrderStatus throws OrderNotFound if order is missing.
     *//*

    @Test
    void testUpdateOrderStatus_OrderNotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFound.class, () -> orderService.updateOrderStatus(orderId, OrderStatus.PAID));
    }

    */
/**
     * Verifies getBuyerOrder returns all order items for a user.
     *//*

    @Test
    void testGetBuyerOrder() {
        UUID userId = UUID.randomUUID();
        OrderItem item1 = new OrderItem();
        OrderItem item2 = new OrderItem();
        Set<OrderItem> items = new HashSet<>();
        items.add(item1);
        items.add(item2);
//        when(orderItemRepository.findByUserId(userId)).thenReturn(items);
        Set<OrderItem> result = orderService.getBuyerOrder(userId);
        assertEquals(2, result.size());
        assertTrue(result.contains(item1));
        assertTrue(result.contains(item2));
    }
} */
