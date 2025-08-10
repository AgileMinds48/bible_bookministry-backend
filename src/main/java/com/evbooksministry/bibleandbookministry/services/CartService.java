package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.dtos.AddOrRemoveFromCartRequest;
import com.evbooksministry.bibleandbookministry.enums.DeleteYn;
import com.evbooksministry.bibleandbookministry.enums.OrderStatus;
import com.evbooksministry.bibleandbookministry.exceptions.BookNotFound;
import com.evbooksministry.bibleandbookministry.exceptions.CustomerNotFound;
import com.evbooksministry.bibleandbookministry.exceptions.EmptyCart;
import com.evbooksministry.bibleandbookministry.exceptions.UserNotFoundException;
import com.evbooksministry.bibleandbookministry.models.*;
import com.evbooksministry.bibleandbookministry.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CartService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public CartService(UserRepository userRepository,
                       BookRepository bookRepository,
                       CustomerRepository customerRepository,
                       OrderRepository orderRepository,
                       OrderItemRepository orderItemRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public Set<OrderItem> addItemToCart(AddOrRemoveFromCartRequest request, UUID userID) {
        Book book = bookRepository.findByBookId(request.bookId())
                .orElseThrow(BookNotFound::new);

        Users user = userRepository.findById(userID)
                .orElseThrow(UserNotFoundException::new);

        Customer customer = customerRepository.getCustomerByUserId(user.getUserId())
                .orElseThrow(CustomerNotFound::new);

        CustomerOrders existingOrder = null;
        try {
            existingOrder = orderRepository.getCustomerOrdersByCustomerId(customer.getCustomerId());
        } catch (RuntimeException e) {
        }

        if (existingOrder != null && existingOrder.getOrderStatus() == OrderStatus.IN_CART) {
            Set<OrderItem> existingItems = existingOrder.getOrderItems();
            if (existingItems == null) {
                existingItems = new HashSet<>();
                existingOrder.setOrderItems(existingItems);
            }

            Optional<OrderItem> existingItem = existingItems.stream()
                    .filter(item -> item.getBook().getBookId().equals(request.bookId()))
                    .findFirst();

            if (existingItem.isPresent()) {
                OrderItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + request.quantity());
                orderItemRepository.save(item);
            } else {
                OrderItem newItem = createOrderItem(book, request.quantity());
                orderItemRepository.save(newItem);
                existingItems.add(newItem);
            }

            orderRepository.save(existingOrder);
            return existingItems;
        } else {
            Set<OrderItem> customerOrderItems = new HashSet<>();
            OrderItem item = createOrderItem(book, request.quantity());
            orderItemRepository.save(item);
            customerOrderItems.add(item);

            CustomerOrders order = new CustomerOrders();
            order.setCustomerId(customer);
            order.setOrderStatus(OrderStatus.IN_CART);
            order.setOrderReference(UUID.randomUUID().toString());
            order.setOrderItems(customerOrderItems);
            order.setDeleteYn(DeleteYn.N); // Set default value

            orderRepository.save(order);

            return customerOrderItems;
        }
    }

    private OrderItem createOrderItem(Book book, Integer quantity) {
        OrderItem item = new OrderItem();
        item.setQuantity(quantity);
        item.setUnitPrice(book.getBookPrice());
        item.setBook(book);
        return item;
    }

    @Transactional
    public Set<OrderItem> removeItemFromCart(AddOrRemoveFromCartRequest request, UUID userID) {
        Users users = userRepository.findById(userID)
                .orElseThrow(UserNotFoundException::new);

        Customer customer = customerRepository.getCustomerByUserId(users.getUserId())
                .orElseThrow(CustomerNotFound::new);

        CustomerOrders customerOrder = orderRepository.getCustomerOrdersByCustomerId(customer.getCustomerId());

        if (customerOrder == null || customerOrder.getOrderItems() == null) {
            throw new EmptyCart("Your cart is empty");
        }

        Set<OrderItem> orderItems = customerOrder.getOrderItems();
        Optional<OrderItem> itemToRemove = orderItems.stream()
                .filter(cartItem -> cartItem.getBook().getBookId().equals(request.bookId()))
                .findFirst();

        if (itemToRemove.isPresent()) {
            OrderItem item = itemToRemove.get();
            orderItems.remove(item);
            orderItemRepository.delete(item); // Actually remove from database
        }

        orderRepository.save(customerOrder);
        return orderItems;
    }

    @Transactional
    public void clearCart(UUID userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
        Customer customer = customerRepository.getCustomerByUserId(users.getUserId())
                .orElseThrow(CustomerNotFound::new);

        CustomerOrders customerOrder = orderRepository.getCustomerOrdersByCustomerId(customer.getCustomerId());

        if (customerOrder != null) {
            if (customerOrder.getOrderItems() != null) {
                customerOrder.getOrderItems().clear();
            }

            customerOrder.setDeleteYn(DeleteYn.Y);
            orderRepository.save(customerOrder);
        }
    }

    public Set<OrderItem> fetchCartItems(UUID userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        Customer customer = customerRepository.getCustomerByUserId(users.getUserId())
                .orElseThrow(CustomerNotFound::new);

        try {
            CustomerOrders customerOrder = orderRepository.getCustomerOrdersByCustomerId(customer.getCustomerId());

            if (customerOrder == null || customerOrder.getDeleteYn() == DeleteYn.Y) {
                throw new EmptyCart("Your cart is empty");
            }

            Set<OrderItem> cart = customerOrder.getOrderItems();

            if (cart == null || cart.isEmpty()) {
                throw new EmptyCart("Your cart is empty");
            }

            return new HashSet<>(cart);
        } catch (RuntimeException e) {
            if (e instanceof EmptyCart) {
                throw e;
            }
            throw new EmptyCart("Your cart is empty");
        }
    }

    public Set<OrderItem> fetchUserCart(UUID userId) {
        Set<OrderItem> cartItems = new HashSet<>(orderItemRepository.getCustomerCart(userId));

        if (cartItems.isEmpty()) {
            throw new EmptyCart("Your cart is empty");
        }

        return cartItems;
    }
}