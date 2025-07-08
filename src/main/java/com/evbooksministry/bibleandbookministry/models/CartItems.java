package com.evbooksministry.bibleandbookministry.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class CartItems {
    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart_Id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;


    private int quantity;
    private BigDecimal price;


    public UUID getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(UUID cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CartItems{" +
                "cartItemId=" + cartItemId +
                ", cart=" + cart +
                ", book=" + book +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
