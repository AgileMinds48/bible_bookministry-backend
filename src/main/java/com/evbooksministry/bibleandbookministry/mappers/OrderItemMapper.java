package com.evbooksministry.bibleandbookministry.mappers;

import com.evbooksministry.bibleandbookministry.dtos.OrderItemDTO;
import com.evbooksministry.bibleandbookministry.models.Book;
import com.evbooksministry.bibleandbookministry.models.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderItemMapper {
    /**
     * Convert OrderItem entity to OrderItemDTO
     */
    public OrderItemDTO toDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        Integer quantity = orderItem.getQuantity();
        BigDecimal price = orderItem.getUnitPrice();
        BigDecimal total = price.multiply(BigDecimal.valueOf(quantity));

        OrderItemDTO dto = OrderItemDTO.builder()
                .orderItemId(orderItem.getOrderItemId())
                .orderId(orderItem.getCustomerOrderId() != null ?
                        orderItem.getCustomerOrderId().getOrderId() : null)
                .bookId(orderItem.getBookId() != null ?
                        orderItem.getBookId().getBookId() : null)
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .total(total)
                .deleteYn(orderItem.getDeleteYn())
                .build();

        if (orderItem.getBookId() != null) {
            Book book = orderItem.getBookId();
            dto.setBookTitle(book.getBookTitle());
            dto.setBookAuthor(book.getBookAuthor());
            dto.setBookImageUrl(book.getMedia());

            OrderItemDTO.BookDetailsDTO bookDetails = new OrderItemDTO.BookDetailsDTO(
                    book.getBookId(),
                    book.getBookTitle(),
                    book.getBookAuthor(),
                    book.getMedia(),
                    book.getBookCategory(),
                    book.getBookPrice(),
                    book.getBookPrice(),
                    book.getAmountInStock()
            );
            dto.setBookDetails(bookDetails);
        }

        return dto;
    }

    /**
     * Convert OrderItemDTO to OrderItem entity
     */
    public OrderItem toEntity(OrderItemDTO dto) {
        if (dto == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(dto.getOrderItemId());
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setUnitPrice(dto.getUnitPrice());
        orderItem.setTotal(dto.getTotal());
        orderItem.setDeleteYn(dto.getDeleteYn());

        return orderItem;
    }

}