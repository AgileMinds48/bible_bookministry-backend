package com.evbooksministry.bibleandbookministry.dtos;

import com.evbooksministry.bibleandbookministry.enums.DeleteYn;
import com.evbooksministry.bibleandbookministry.models.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDTO {

    private UUID orderItemId;

    private UUID orderId;

    private UUID bookId;

    // Book details for display purposes
    private String bookTitle;
    private String bookAuthor;

    private List<String> bookImageUrl;
    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal total;

    private DeleteYn deleteYn;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BookDetailsDTO {
        private UUID bookId;
        private String title;
        private String author;
        private String isbn;
        private List<String> imageUrl;
        private Category category;
        private BigDecimal originalPrice;
        private BigDecimal currentPrice;
        private Integer stockQuantity;

        public BookDetailsDTO() {}

        public BookDetailsDTO(UUID bookId, String title, String author, List<String> imageUrl, Category category, BigDecimal originalPrice, BigDecimal currentPrice, Integer stockQuantity) {
            this.bookId = bookId;
            this.title = title;
            this.author = author;
            this.imageUrl = imageUrl;
            this.category = category;
            this.originalPrice = originalPrice;
            this.currentPrice = currentPrice;
            this.stockQuantity = stockQuantity;
        }

        public UUID getBookId() { return bookId; }
        public void setBookId(UUID bookId) { this.bookId = bookId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }


        public List<String> getImageUrl() { return imageUrl; }
        public void setImageUrl(List<String> imageUrl) { this.imageUrl = imageUrl; }

        public Category getCategory() { return category; }
        public void setCategory(Category category) { this.category = category; }

        public BigDecimal getOriginalPrice() { return originalPrice; }
        public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

        public BigDecimal getCurrentPrice() { return currentPrice; }
        public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }

        public Integer getStockQuantity() { return stockQuantity; }
        public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    }

    private BookDetailsDTO bookDetails;
}