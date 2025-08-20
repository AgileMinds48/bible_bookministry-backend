package com.evbooksministry.bibleandbookministry.serviceInterfaces;

import com.evbooksministry.bibleandbookministry.dtos.BuyNow;
import com.evbooksministry.bibleandbookministry.dtos.PaymentResponse;
import com.evbooksministry.bibleandbookministry.enums.OrderStatus;
import com.evbooksministry.bibleandbookministry.models.CustomerOrders;
import com.evbooksministry.bibleandbookministry.models.OrderItem;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IOrderService {
    PaymentResponse checkout(UUID userId);

    PaymentResponse buyNow(BuyNow request) throws JsonProcessingException;

    Optional<CustomerOrders> getOrderById(UUID id);

    Set<CustomerOrders> getBuyerOrders(UUID buyerID);

    CustomerOrders updateOrderStatus(UUID orderId, OrderStatus status);

    Set<OrderItem> getBuyerOrder(UUID userId);

//    CustomerOrderDTO createOrder(UUID customerId);

    void cancelOrder(UUID orderId);
}

