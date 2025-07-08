package com.evbooksministry.bibleandbookministry.repositories;

import com.evbooksministry.bibleandbookministry.enums.OrderStatus;
import com.evbooksministry.bibleandbookministry.models.CustomerOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrders, UUID> {

    @Query("select c from CustomerOrders c where c.user.userId  = :userId and c.orderStatus = :orderStatus")
    Set<CustomerOrders> findByUser_UserIdAndStatus(UUID userId, OrderStatus orderStatus);

    Set<CustomerOrders> findByUser_UserId(UUID buyerID);

    List<CustomerOrders> findByOrderReference(String orderReference);
}
