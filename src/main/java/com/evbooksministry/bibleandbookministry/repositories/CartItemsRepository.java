package com.evbooksministry.bibleandbookministry.repositories;

import com.evbooksministry.bibleandbookministry.models.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface CartItemsRepository extends JpaRepository<CartItems, UUID> {
    Set<CartItems> findCartItemsByCart_Users_UserId(UUID userId);
}
