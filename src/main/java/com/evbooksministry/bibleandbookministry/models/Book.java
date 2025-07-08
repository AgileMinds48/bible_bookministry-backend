package com.evbooksministry.bibleandbookministry.models;

import com.evbooksministry.bibleandbookministry.converter.StringListConverter;
import com.evbooksministry.bibleandbookministry.enums.BookCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "books",
        indexes = {
                @Index(name = "idx_book_title", columnList = "bookTitle")
        }
)
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookId;

    @Size(max = 255)
    @Column(nullable = false)
    private String bookTitle;

    @Size(max = 255)
    @Column(nullable = false)
    private String bookDescription;

    @Size(max = 255)
    @Column(nullable = false)
    private BigDecimal bookPrice;


    private Integer quantity;
    /**
     * book price by the quantity
     */
    private BigDecimal bookValue;
    private Integer amountSold;
    private Integer amountInStock;
    private boolean isAvailable;

    @CreationTimestamp
    private Timestamp createdOn;

    @UpdateTimestamp
    private Timestamp updatedOn;

    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(attributeName = "media", converter = StringListConverter.class)
    private List<String> media = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private BookCategory bookCategory;

    @PrePersist
    protected void onCreate(){
        this.createdOn = Timestamp.from(Instant.now());
        this.bookValue = this.bookPrice.multiply(BigDecimal.valueOf(this.quantity));
        this.amountSold = 0;
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedOn = Timestamp.from(Instant.now());
        this.bookValue =
                this.bookPrice.multiply(BigDecimal.valueOf(this.quantity));
    }


    public Book(UUID bookId, String bookTitle, String bookDescription, BigDecimal bookPrice, Integer quantity, BigDecimal bookValue, Integer amountSold, Integer amountInStock, boolean isAvailable, Timestamp createdOn, Timestamp updatedOn, List<String> media, BookCategory bookCategory) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookDescription = bookDescription;
        this.bookPrice = bookPrice;
        this.quantity = quantity;
        this.bookValue = bookValue;
        this.amountSold = amountSold;
        this.amountInStock = amountInStock;
        this.isAvailable = isAvailable;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.media = media;
        this.bookCategory = bookCategory;
    }

    public Book() {
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public BigDecimal getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(BigDecimal bookPrice) {
        this.bookPrice = bookPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getBookValue() {
        return bookValue;
    }

    public void setBookValue(BigDecimal bookValue) {
        this.bookValue = bookValue;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getAmountSold() {
        return amountSold;
    }

    public void setAmountSold(Integer amountSold) {
        this.amountSold = amountSold;
    }

    public Integer getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(Integer amountInStock) {
        this.amountInStock = amountInStock;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public List<String> getMedia() {
        return media;
    }

    public void setMedia(List<String> media) {
        this.media = media;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }
}
