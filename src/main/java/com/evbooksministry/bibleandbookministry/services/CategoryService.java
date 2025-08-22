package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.dtos.CategoryDTO;
import com.evbooksministry.bibleandbookministry.exceptions.CategoryAlreadyExists;
import com.evbooksministry.bibleandbookministry.mappers.CategoryMapper;
import com.evbooksministry.bibleandbookministry.models.Category;
import com.evbooksministry.bibleandbookministry.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    public CategoryDTO createNewBookCategory(CategoryDTO categoryDTO){
        List<String> existingCategoryNames = categoryRepository.getAllCategories()
                .stream()
                .map(Category::getCategoryName)
                .toList();
        if(existingCategoryNames.contains(categoryDTO.categoryName())){
            throw new CategoryAlreadyExists();
        }
        Category newCategory = categoryMapper.categoryDTOToEntity(categoryDTO);
        return categoryMapper.categoryEntity(
                categoryRepository.save(newCategory)
        );
    }

    public List<CategoryDTO> createDefaultCategories(List<CategoryDTO> list){
        List<Category> categories = new ArrayList<>();
        for(CategoryDTO dto : list){
            categories.add(categoryRepository.save(categoryMapper.categoryDTOToEntity(dto)));
        }
        return categories
                .stream()
                .map(categoryMapper::categoryEntity)
                .toList();
    }
}
