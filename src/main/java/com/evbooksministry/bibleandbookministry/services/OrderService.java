package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.config.EmailService;
import com.evbooksministry.bibleandbookministry.dtos.BuyNow;
import com.evbooksministry.bibleandbookministry.dtos.OrderDTO;
import com.evbooksministry.bibleandbookministry.dtos.PaymentResponse;
import com.evbooksministry.bibleandbookministry.enums.DeleteYn;
import com.evbooksministry.bibleandbookministry.enums.OrderStatus;
import com.evbooksministry.bibleandbookministry.exceptions.*;
import com.evbooksministry.bibleandbookministry.models.*;
import com.evbooksministry.bibleandbookministry.repositories.*;
import com.evbooksministry.bibleandbookministry.serviceInterfaces.IOrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.evbooksministry.bibleandbookministry.enums.OrderStatus.*;

@Service
public class OrderService implements IOrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final EmailService emailService;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final OrderItemRepository orderItemRepository;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;

    public OrderService(UserRepository userRepository,
                        OrderRepository orderRepository,
                        EmailService emailService,
                        PaymentService paymentService,
                        PaymentRepository paymentRepository,
                        OrderItemRepository orderItemRepository,
                        BookRepository bookRepository,
                        CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.emailService = emailService;
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.orderItemRepository = orderItemRepository;
        this.bookRepository = bookRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public PaymentResponse checkout(UUID userId) {
        logger.info("Starting checkout for user: {}", userId);

        Users user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Customer customer = customerRepository.getCustomerByUserId(user.getUserId())
                .orElseThrow(CustomerNotFound::new);

        CustomerOrders customerOrder = orderRepository.getCustomerOrdersByCustomerId(customer.getCustomerId());

        Set<OrderItem> userCart = customerOrder.getOrderItems();

        if (userCart == null || userCart.isEmpty()) {
            throw new EmptyCart("Cart is empty");
        }

        // Validate stock availability before creating order
        validateStockAvailability(userCart);

        // Create the order
        CustomerOrders customerOrders = createOrderFromCart(customer, userCart);

        try {
            // Process payment
            PaymentResponse paymentResponse = processOrderPayment(user, customerOrders);

            // Update inventory and sales after successful payment
            updateInventoryAndSales(customerOrders.getOrderItems());

            // Clear the cart after successful checkout
            clearUserCart(customer.getCustomerId());

            // Send confirmation email (async)
            sendOrderConfirmationEmail(user, customerOrders);

            logger.info("Checkout completed successfully for user: {} with order: {}",
                    userId, customerOrders.getOrderReference());

            return paymentResponse;

        } catch (Exception e) {
            logger.error("Payment processing failed for user: {} with order: {}",
                    userId, customerOrders.getOrderReference(), e);

            // Update order status to failed
            customerOrders.setOrderStatus(CANCELLED);
            orderRepository.save(customerOrders);

            throw new RuntimeException("Payment processing failed: " + e.getMessage(), e);
        }
    }

    private void validateStockAvailability(Set<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            Book book = orderItem.getBook();
            if (book.getAmountInStock() < orderItem.getQuantity()) {
                throw new InsufficientStock();
            }
        }
    }

    private CustomerOrders createOrderFromCart(Customer customer, Set<OrderItem> cartItems) {
        CustomerOrders order = new CustomerOrders();
        order.setCustomerId(customer);
        order.setCreatedAt(Timestamp.from(Instant.now()));
        order.setOrderStatus(PENDING);
        order.setOrderReference(UUID.randomUUID().toString());
        order.setDeleteYn(DeleteYn.N);

        // Create new order items (don't reuse cart items)
        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItem cartItem : cartItems) {
            OrderItem orderItem = createOrderItemFromCart(cartItem, order);
            orderItems.add(orderItem);
            totalPrice = totalPrice.add(orderItem.getTotal());
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        // Save order first, then items
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        logger.debug("Created order with {} items, total: {}", orderItems.size(), totalPrice);

        return order;
    }

    private OrderItem createOrderItemFromCart(OrderItem cartItem, CustomerOrders order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setUnitPrice(cartItem.getUnitPrice());
        orderItem.setTotal(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

        return orderItem;
    }

    private OrderItem createOrderItem(OrderItem cartItem, CustomerOrders customerOrders) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(customerOrders);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());

        BigDecimal unitPrice = cartItem.getPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        orderItem.setUnitPrice(unitPrice);
        orderItem.setTotal(totalPrice);

        return orderItem;
    }

    private PaymentResponse processOrderPayment(Users user, CustomerOrders customerOrders)
            throws JsonProcessingException {
        PaymentResponse paymentResponse = paymentService.processPayment(
                user.getEmail(),
                customerOrders.getTotalPrice()
        );

        // Create payment record
        Payment payment = new Payment();
        payment.setPaymentDate(Timestamp.from(Instant.now()));
        payment.setAmount(customerOrders.getTotalPrice());
        payment.setCustomerOrders(customerOrders);
        payment.setPaymentReference(paymentResponse.data().reference());
        paymentRepository.save(payment);

        // Update order with payment info and mark as paid
        customerOrders.setOrderPayment(payment);
        customerOrders.setOrderReference(paymentResponse.data().reference());
        customerOrders.setOrderStatus(PAID);
        orderRepository.save(customerOrders);

        return paymentResponse;
    }

    private void updateInventoryAndSales(Set<OrderItem> cartItems) {
        for (OrderItem cartItem : cartItems) {
            Book book = cartItem.getBook();

            // Update sales count
            int currentSold = book.getAmountSold() != null ? book.getAmountSold() : 0;
            book.setAmountSold(currentSold + cartItem.getQuantity());

            // Update stock
            book.setAmountInStock(book.getAmountInStock() - cartItem.getQuantity());

            bookRepository.save(book);
        }
    }

    private void clearUserCart(UUID customerId) {

        // Assuming you have a CartRepository to save the cleared cart
        // cartRepository.save(cart);
    }

    private void sendOrderConfirmationEmail(Users user, CustomerOrders order) {
        try {
            // Implement email sending logic
            emailService.sendOrderConfirmation(user.getEmail(), order);
        } catch (Exception e) {
            logger.error("Failed to send order confirmation email to: {}", user.getEmail(), e);
            // Don't fail the order for email issues
        }
    }

    @Transactional
    public PaymentResponse buyNow(BuyNow request) throws JsonProcessingException {
        logger.info("Processing buy now for user: {} and book: {}", request.userID(), request.bookId());

        Users user = userRepository.findById(request.userID())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new OrderNotFound("Product not found"));

        Customer customer = customerRepository.getCustomerByUserId(user.getUserId())
                .orElseThrow(CustomerNotFound::new);

        // Validate stock
        if (book.getAmountInStock() < request.quantity()) {
            throw new InsufficientStock();
        }

        BigDecimal unitPrice = book.getBookPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(request.quantity()));

        // Create order
        CustomerOrders customerOrders = new CustomerOrders();
        customerOrders.setCustomerId(customer);
        customerOrders.setCreatedAt(Timestamp.from(Instant.now()));
        customerOrders.setOrderStatus(PENDING);
        customerOrders.setOrderReference(UUID.randomUUID().toString());
        customerOrders.setTotalPrice(totalPrice);

        // Create order item
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(customerOrders);
        orderItem.setBook(book);
        orderItem.setQuantity(request.quantity());
        orderItem.setUnitPrice(unitPrice);
        orderItem.setTotal(totalPrice);

        customerOrders.setOrderItems(new HashSet<>(Set.of(orderItem)));

        // Save order
        orderRepository.save(customerOrders);
        orderItemRepository.save(orderItem);

        try {
            // Process payment
            PaymentResponse paymentResponse = paymentService.processPayment(
                    user.getEmail(), totalPrice);

            // Create payment record
            Payment payment = new Payment();
            payment.setPaymentDate(Timestamp.from(Instant.now()));
            payment.setAmount(totalPrice);
            payment.setCustomerOrders(customerOrders);
            payment.setPaymentReference(paymentResponse.data().reference());
            paymentRepository.save(payment);

            // Update order
            customerOrders.setOrderPayment(payment);
            customerOrders.setOrderReference(paymentResponse.data().reference());
            customerOrders.setOrderStatus(PAID);
            orderRepository.save(customerOrders);

            // Update book inventory and sales
            int currentSold = book.getAmountSold() != null ? book.getAmountSold() : 0;
            book.setAmountSold(currentSold + request.quantity());
            book.setAmountInStock(book.getAmountInStock() - request.quantity());
            bookRepository.save(book);

            // Send confirmation email
            sendOrderConfirmationEmail(user, customerOrders);

            logger.info("Buy now completed successfully for user: {} with order: {}",
                    request.userID(), customerOrders.getOrderReference());

            return paymentResponse;

        } catch (Exception e) {
            logger.error("Buy now payment failed for user: {} with order: {}",
                    request.userID(), customerOrders.getOrderReference(), e);

            customerOrders.setOrderStatus(FAILED);
            orderRepository.save(customerOrders);

            throw new RuntimeException("Payment processing failed: " + e.getMessage(), e);
        }
    }

    public Optional<CustomerOrders> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public Set<CustomerOrders> getBuyerOrders(UUID customerId) {
        return orderRepository.findUserOrders(customerId);
    }

    @Transactional
    public CustomerOrders updateOrderStatus(UUID orderId, OrderStatus status) {
        CustomerOrders customerOrders = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFound::new);

        OrderStatus previousStatus = customerOrders.getOrderStatus();
        customerOrders.setOrderStatus(status);

        CustomerOrders savedOrder = orderRepository.save(customerOrders);

        logger.info("Order status updated from {} to {} for order: {}",
                previousStatus, status, orderId);

        return savedOrder;
    }

    public Set<OrderItem> getBuyerOrder(UUID userId) {
        return orderItemRepository.findByCustomerId(userId);
    }

    @Override
    @Transactional
    public OrderDTO createOrder(UUID customerId) {
        // Implementation for creating order DTO
        Users user = userRepository.findById(customerId)
                .orElseThrow(UserNotFoundException::new);

        // Create order DTO logic here
        OrderDTO orderDTO = new OrderDTO();
        // Set DTO properties

        return orderDTO;
    }

    @Override
    @Transactional
    public void cancelOrder(UUID orderId) {
        CustomerOrders order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFound::new);

        // Only allow cancellation of pending or paid orders
        if (order.getOrderStatus() == SHIPPED || order.getOrderStatus() == DELIVERED) {
            throw new IllegalStateException("Cannot cancel shipped or delivered orders");
        }

        // Restore inventory if order was paid
        if (order.getOrderStatus() == PAID) {
            restoreInventory(order.getOrderItems());
        }

        order.setOrderStatus(CANCELLED);
        orderRepository.save(order);

        logger.info("Order cancelled: {}", orderId);
    }

    private void restoreInventory(Set<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            Book book = item.getBook();
            book.setAmountInStock(book.getAmountInStock() + item.getQuantity());

            // Reduce sales count
            int currentSold = book.getAmountSold() != null ? book.getAmountSold() : 0;
            book.setAmountSold(Math.max(0, currentSold - item.getQuantity()));

            bookRepository.save(book);
        }
    }
}