/**
 * Unit tests for PaymentService covering processPayment success and error scenarios.
 * Each test uses Mockito to mock dependencies and is documented for clarity.
 */
package com.evbooksministry.bibleandbookministry;

import com.evbooksministry.bibleandbookministry.dtos.PaymentRequest;
import com.evbooksministry.bibleandbookministry.dtos.PaymentResponse;
import com.evbooksministry.bibleandbookministry.enums.OrderStatus;
import com.evbooksministry.bibleandbookministry.models.CustomerOrders;
import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.*;
import com.evbooksministry.bibleandbookministry.services.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartItemsRepository cartItemRepository;
    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private PaymentService paymentService;

    /**
     * Sets up mocks and the PaymentService instance before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentService(restTemplate, paymentRepository, userRepository, orderRepository, cartItemRepository, cartRepository);
    }

    /**
     * Verifies processPayment returns PaymentResponse on success.
     */
    @Test
    void testProcessPayment_Success() throws JsonProcessingException {
        String userEmail = "test@example.com";
        BigDecimal totalAmount = new BigDecimal("100.00");
        Users user = mock(Users.class);
        UUID userId = UUID.randomUUID();
        when(user.getUserId()).thenReturn(userId);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        CustomerOrders order = new CustomerOrders();
        Set<CustomerOrders> orders = Collections.singleton(order);
        when(orderRepository.findByUser_UserIdAndStatus(userId, OrderStatus.PENDING)).thenReturn(orders);
        PaymentResponse.Data data = new PaymentResponse.Data("http://test.com", "code123", "ref123", new BigDecimal("10000"));
        PaymentResponse paymentResponse = new PaymentResponse(true, "Success", data);
        ResponseEntity<PaymentResponse> responseEntity = ResponseEntity.ok(paymentResponse);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(PaymentResponse.class))).thenReturn(responseEntity);

        PaymentResponse result = paymentService.processPayment(userEmail, totalAmount);
        assertNotNull(result);
        assertTrue(result.status());
        assertEquals("Success", result.message());
    }

    /**
     * Verifies processPayment throws if user is not found.
     */
    @Test
    void testProcessPayment_UserNotFound() {
        String userEmail = "notfound@example.com";
        BigDecimal totalAmount = new BigDecimal("100.00");
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> paymentService.processPayment(userEmail, totalAmount));
    }

    /**
     * Verifies processPayment throws if no pending orders are found.
     */
    @Test
    void testProcessPayment_OrdersNotFound() {
        String userEmail = "test@example.com";
        BigDecimal totalAmount = new BigDecimal("100.00");
        Users user = mock(Users.class);
        UUID userId = UUID.randomUUID();
        when(user.getUserId()).thenReturn(userId);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(orderRepository.findByUser_UserIdAndStatus(userId, OrderStatus.PENDING)).thenReturn(Collections.emptySet());
        assertThrows(RuntimeException.class, () -> paymentService.processPayment(userEmail, totalAmount));
    }

    /**
     * Verifies processPayment throws if RestTemplate throws an error.
     */
    @Test
    void testProcessPayment_HttpError() {
        String userEmail = "test@example.com";
        BigDecimal totalAmount = new BigDecimal("100.00");
        Users user = mock(Users.class);
        UUID userId = UUID.randomUUID();
        when(user.getUserId()).thenReturn(userId);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        CustomerOrders order = new CustomerOrders();
        Set<CustomerOrders> orders = Collections.singleton(order);
        when(orderRepository.findByUser_UserIdAndStatus(userId, OrderStatus.PENDING)).thenReturn(orders);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(PaymentResponse.class))).thenThrow(new RuntimeException("HTTP error"));
        assertThrows(RuntimeException.class, () -> paymentService.processPayment(userEmail, totalAmount));
    }
} 