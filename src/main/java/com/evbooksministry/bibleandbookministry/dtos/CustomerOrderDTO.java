package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.enums.DeleteYn;
import com.evbooksministry.bibleandbookministry.enums.OrderStatus;
import com.evbooksministry.bibleandbookministry.models.Customer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

public record CustomerOrderDTO(
        UUID orderId,
        Customer customerId,
        Set<OrderItemDTO> orderItems,
        Timestamp createdAt,
        Timestamp updatedAt,
        OrderStatus orderStatus,
        BigDecimal totalPrice,
        String orderReference,
        DeleteYn deleteYn

) {
}
