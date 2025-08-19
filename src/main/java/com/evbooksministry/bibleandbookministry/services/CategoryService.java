package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.dtos.CategoryDTO;
import com.evbooksministry.bibleandbookministry.exceptions.CategoryAlreadyExists;
import com.evbooksministry.bibleandbookministry.models.Category;
import com.evbooksministry.bibleandbookministry.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public CategoryDTO createNewBookCategory(CategoryDTO categoryDTO){
        List<String> existingCategoryNames = categoryRepository.getAllCategories()
                .stream()
                .map(Category::getCategoryName)
                .toList();
        if(existingCategoryNames.contains(categoryDTO.categoryName())){
            throw new CategoryAlreadyExists();
        }

    }
}
