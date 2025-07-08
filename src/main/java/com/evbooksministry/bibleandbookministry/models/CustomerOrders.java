package com.evbooksministry.bibleandbookministry.models;

import com.evbooksministry.bibleandbookministry.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "customer_orders")
public class CustomerOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @OneToOne(cascade = CascadeType.ALL)
    private Users user;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CartItems> cartItems;

    @OneToMany
    private Set<OrderItem> orderItems;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private OrderStatus orderStatus;

    private BigDecimal totalPrice;

    private String orderReference;

    @OneToOne
    private Payment orderPayment;

    public Set<CartItems> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItems> cartItems) {
        this.cartItems = cartItems;
    }


    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void removeProduct(UUID productId) {

    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }

    public Payment getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(Payment orderPayment) {
        this.orderPayment = orderPayment;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "CustomerOrders{" +
                "cartId=" + orderId +
                ", user=" + user +
                ", cartItems=" + cartItems +
                ", orderItems=" + orderItems +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", orderStatus=" + orderStatus +
                ", totalPrice=" + totalPrice +
                ", orderReference='" + orderReference + '\'' +
                ", orderPayment=" + orderPayment +
                '}';
    }
}
