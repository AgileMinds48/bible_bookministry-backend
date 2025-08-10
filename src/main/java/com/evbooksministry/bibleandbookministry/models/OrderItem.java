package com.evbooksministry.bibleandbookministry.models;

import com.evbooksministry.bibleandbookministry.enums.DeleteYn;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private CustomerOrders customerOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId")
    private Book bookId;

    private Integer quantity;

    @Column(nullable = false, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    @PrePersist
    protected void onCreate(){
        this.total = unitPrice.multiply(BigDecimal.valueOf(quantity));
        this.deleteYn = DeleteYn.N;
    }

    public OrderItem(UUID orderItemId,
                     CustomerOrders customerOrderId,
                     Book bookId,
                     Integer quantity,
                     BigDecimal unitPrice,
                     BigDecimal total,
                     DeleteYn deleteYn) {
        this.orderItemId = orderItemId;
        this.customerOrderId = customerOrderId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = total;
        this.deleteYn = deleteYn;
    }

    public OrderItem() {
    }

    public CustomerOrders getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(CustomerOrders customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public Book getBookId() {
        return bookId;
    }

    public void setBookId(Book bookId) {
        this.bookId = bookId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public UUID getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(UUID orderItemId) {
        this.orderItemId = orderItemId;
    }

    public CustomerOrders getOrder() {
        return customerOrderId;
    }

    public void setOrder(CustomerOrders customerOrders) {
        this.customerOrderId = customerOrders;
    }

    public Book getBook() {
        return bookId;
    }

    public void setBook(Book bookId) {
        this.bookId = bookId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return unitPrice;
    }

    public void setPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", customerOrderId=" + customerOrderId +
                ", bookId=" + bookId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", total=" + total +
                '}';
    }
}
