package com.evbooksministry.bibleandbookministry.repositories;

import com.evbooksministry.bibleandbookministry.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    @Query("select o from OrderItem o where o.customerOrders.user.userId = :userId order by o.customerOrders.createdAt")
    Set<OrderItem> findByUserId(UUID userId);
}
