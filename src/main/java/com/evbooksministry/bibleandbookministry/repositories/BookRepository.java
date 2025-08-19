package com.evbooksministry.bibleandbookministry.repositories;

import com.evbooksministry.bibleandbookministry.dtos.BookDTO;
import com.evbooksministry.bibleandbookministry.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    @Query("select b from Book b where b.isAvailable = TRUE")
    Page<BookDTO> getAllByAvailable(Pageable pageable);

    @Query("select b from Book b where b.bookId = :bookId")
    Optional<Book> findByBookId(UUID bookId);

    @Query("select b from Book b where b.bookCategory.categoryName = :categoryName")
    Page<Book> findByBookCategory(String categoryName, Pageable pageable);

    @Query("select count(b) from Book b where b.isAvailable = true and b.deleteYn = 'NO'")
    Integer countTotalAvailableBooks();

    @Query("select b from Book b where b.amountInStock < 0")
    List<Book> getBookLowInStock();

    @Query("select b from Book b order by b.amountSold desc")
    List<Book> getBooksByAmountSold();

}
