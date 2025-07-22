package com.evbooksministry.bibleandbookministry.repositories;

import com.evbooksministry.bibleandbookministry.models.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;
import java.util.UUID;

public interface CartItemsRepository extends JpaRepository<CartItems, UUID> {

    @Query("select c from CartItems c where c.cart.users.userId = :userId ")
    Set<CartItems> findCartItemsByUser(UUID userId);
}
