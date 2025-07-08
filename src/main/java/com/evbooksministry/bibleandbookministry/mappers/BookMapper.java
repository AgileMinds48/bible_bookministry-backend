package com.evbooksministry.bibleandbookministry.mappers;

import com.evbooksministry.bibleandbookministry.dtos.BookDTO;
import com.evbooksministry.bibleandbookministry.models.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book bookDTOToBookEntity(BookDTO bookDTO);

    BookDTO bookEntityToBookDTO(Book book);
}
