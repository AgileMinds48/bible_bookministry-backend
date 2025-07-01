package com.evbooksministry.bibleandbookministry.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Cart {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long cartId;

    @OneToOne(mappedBy = "userCart", cascade = CascadeType.ALL)
    private Users users;

    @OneToMany
    private Set<CartItems> cartItems;


    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
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
}
