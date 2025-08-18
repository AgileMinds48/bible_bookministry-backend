package com.evbooksministry.bibleandbookministry.repositories;

import com.evbooksministry.bibleandbookministry.models.CustomerOrders;
import com.evbooksministry.bibleandbookministry.models.OrderItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

/*    @Query("select o from OrderItem o where o.customerOrders.user.userId = :userId order by o.customerOrders.createdAt")
    Set<OrderItem> findByUserId(UUID userId);*/

    @Query("select o from OrderItem o where o.customerOrderId.customerId.customerId = :customerId and o.deleteYn = 'N'")
    Set<OrderItem> getCustomerCart(UUID customerId);

    @Query("select o from OrderItem o join o.customerOrderId co where co.customerId.customerId = :customerId and co.orderStatus = 'IN_CART'")
    Set<OrderItem> findByCustomerId(UUID customerId);

    @Modifying
    @Transactional
    @Query("delete OrderItem o where o.customerOrderId = :customerOrder")
    void deleteByCustomerOrderId(CustomerOrders customerOrder);
}
