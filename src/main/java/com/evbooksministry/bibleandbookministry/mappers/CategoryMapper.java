package com.evbooksministry.bibleandbookministry.mappers;

import com.evbooksministry.bibleandbookministry.dtos.CategoryDTO;
import com.evbooksministry.bibleandbookministry.models.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category categoryDTOToEntity(CategoryDTO categoryDTO);


    CategoryDTO categoryEntity(Category category);
}
