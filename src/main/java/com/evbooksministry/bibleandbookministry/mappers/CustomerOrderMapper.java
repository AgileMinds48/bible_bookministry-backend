package com.evbooksministry.bibleandbookministry.mappers;

import com.evbooksministry.bibleandbookministry.dtos.CustomerOrderDTO;
import com.evbooksministry.bibleandbookministry.dtos.OrderItemDTO;
import com.evbooksministry.bibleandbookministry.models.Book;
import com.evbooksministry.bibleandbookministry.models.CustomerOrders;
import com.evbooksministry.bibleandbookministry.models.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CustomerOrderMapper {

    CustomerOrderDTO toDTO(CustomerOrders customerOrder);

    OrderItemDTO map(OrderItem orderItem);

    OrderItem map(OrderItemDTO orderItemDTO);

    List<OrderItemDTO> mapOrderItems(List<OrderItem> orderItems);

    Book map(UUID value);

    UUID map(Book value);


}