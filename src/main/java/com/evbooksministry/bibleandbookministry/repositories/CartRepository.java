package com.evbooksministry.bibleandbookministry.repositories;

import com.evbooksministry.bibleandbookministry.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
