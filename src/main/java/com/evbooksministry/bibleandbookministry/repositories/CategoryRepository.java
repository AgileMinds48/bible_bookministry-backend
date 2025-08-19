package com.evbooksministry.bibleandbookministry.repositories;

import com.evbooksministry.bibleandbookministry.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<UUID, Category> {

    @Query("select c from Category c where c.categoryName = :categoryName")
    Optional<Category> findByCategoryName(String categoryName);

    @Query("select c from Category c where c.deleteYn = 'N'")
    List<Category> getAllCategories();
}
