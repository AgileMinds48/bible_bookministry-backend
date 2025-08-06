package com.evbooksministry.bibleandbookministry.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
public class Cart {

    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID cartId;

    @OneToOne(cascade = CascadeType.ALL)
    private Users users;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItems> cartItems;


    private BigDecimal totalPrice;

    @CreationTimestamp
    private Timestamp createdOn;

    public Cart(Users users, Set<CartItems> cartItems) {
        this.cartId = cartId;
        this.users = users;
        this.cartItems = cartItems;
        this.createdOn = Timestamp.from(Instant.now());
    }

    public Cart() {

    }


    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Set<CartItems> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItems> cartItems) {
        this.cartItems = cartItems;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
