package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.config.EmailService;
import com.evbooksministry.bibleandbookministry.dtos.BuyNow;
import com.evbooksministry.bibleandbookministry.dtos.PaymentResponse;
import com.evbooksministry.bibleandbookministry.enums.OrderStatus;
import com.evbooksministry.bibleandbookministry.exceptions.EmptyCart;
import com.evbooksministry.bibleandbookministry.exceptions.OrderNotFound;
import com.evbooksministry.bibleandbookministry.exceptions.UserNotFoundException;
import com.evbooksministry.bibleandbookministry.models.*;
import com.evbooksministry.bibleandbookministry.repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static com.evbooksministry.bibleandbookministry.enums.OrderStatus.PENDING;


@Service
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final EmailService emailService;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final OrderItemRepository orderItemRepository;
    private final BookRepository bookRepository;

    public OrderService(UserRepository userRepository,
                        OrderRepository orderRepository,
                        CartRepository cartRepository,
                        EmailService emailService,
                        PaymentService paymentService,
                        PaymentRepository paymentRepository,
                        OrderItemRepository orderItemRepository,
                        BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.emailService = emailService;
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.orderItemRepository = orderItemRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public PaymentResponse checkout(UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Cart cart = user.getUserCart();

        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new EmptyCart("Cart is empty");
        }

        // creating a new order
        CustomerOrders customerOrders = new CustomerOrders();
        customerOrders.setUser(user);
        customerOrders.setCreatedAt(Timestamp.from(Instant.now()));
        customerOrders.setOrderStatus(PENDING);


        Set<OrderItem> checkoutItems = new HashSet<>();
        for (CartItems cartItem : cart.getCartItems()) {
            OrderItem orderItem = getOrderItem(cartItem, customerOrders);
            checkoutItems.add(orderItem);
        }
        System.out.println("checkout items: " + checkoutItems);

//        customerOrders.setOrderItems(checkoutItems);
        customerOrders.setTotalPrice(checkoutItems
                .stream()
                .map(OrderItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        // Save order first (without payment)
        orderRepository.save(customerOrders);

        System.out.println("customer orders: " + customerOrders);

        // Update product sales
        cart.getCartItems().forEach(cartItem -> {
            Book book = cartItem.getBook();
            book.setAmountSold(book.getAmountSold() + cartItem.getQuantity());
            bookRepository.save(book);
        });

        try {
            // Process payment through Paystack
            PaymentResponse paymentResponse = paymentService.processPayment(
                    user.getEmail(),
                    customerOrders.getTotalPrice()
            );

            // payment record after successful payment processing
            Payment payment = new Payment();
            payment.setPaymentDate(LocalDateTime.now());
            payment.setAmount(customerOrders.getTotalPrice().doubleValue());
            payment.setCustomerOrders(customerOrders);
            paymentRepository.save(payment);

            // Link payment to order and save
            customerOrders.setOrderPayment(payment);
            customerOrders.setOrderReference(paymentResponse.data().reference());
            orderRepository.save(customerOrders);

            return paymentResponse;

        } catch (Exception e) {
            // Handle payment failure
            customerOrders.setOrderStatus(PENDING);
            orderRepository.save(customerOrders);
            throw new RuntimeException("Payment processing failed: " + e.getMessage(), e);
        }
    }

    private static OrderItem getOrderItem(CartItems cartItem, CustomerOrders customerOrders) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(customerOrders);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());

        BigDecimal unitPrice = cartItem.getPrice();

        orderItem.setPrice(unitPrice);
        orderItem.setTotal(unitPrice);
        return orderItem;
    }

    @Transactional
    public PaymentResponse buyNow(BuyNow request) throws JsonProcessingException {
        Users user = userRepository.findById(request.userID())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new OrderNotFound("Product not found"));

        BigDecimal bookPrice = book.getBookPrice();

        CustomerOrders customerOrders = new CustomerOrders();
        customerOrders.setUser(user);
        customerOrders.setCreatedAt(Timestamp.from(Instant.now()));
        customerOrders.setOrderStatus(PENDING);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(customerOrders);
        orderItem.setBook(book);
        orderItem.setQuantity(request.quantity());
        orderItem.setPrice(bookPrice);
        orderItem.setTotal(bookPrice);

        customerOrders.setOrderItems(new HashSet<>(Set.of(orderItem)));
        customerOrders.setTotalPrice(bookPrice);
        orderRepository.save(customerOrders);

        book.setAmountSold(request.quantity());
        book.setAmountInStock(book.getAmountInStock() - request.quantity() );

        return paymentService.processPayment(user.getEmail(), bookPrice);
    }

    public Optional<CustomerOrders> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    @Cacheable(value = "orders")
    public Set<CustomerOrders> getBuyerOrders(UUID buyerID) {
        return orderRepository.findByUser_UserId(buyerID);
    }

    public CustomerOrders updateOrderStatus(UUID orderId, OrderStatus status) {
        CustomerOrders customerOrders = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFound::new);
        customerOrders.setOrderStatus(status);
        return orderRepository.save(customerOrders);
    }

    public Set<OrderItem> getBuyerOrder(UUID userId) {
        return orderItemRepository.findByUserId(userId);
    }
}
