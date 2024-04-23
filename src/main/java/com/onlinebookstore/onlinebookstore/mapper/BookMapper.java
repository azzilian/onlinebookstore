package com.onlinebookstore.onlinebookstore.mapper;

import com.onlinebookstore.onlinebookstore.config.MapperConfig;
import com.onlinebookstore.onlinebookstore.dto.BookDto;
import com.onlinebookstore.onlinebookstore.dto.CreateBookRequestDto;
import com.onlinebookstore.onlinebookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
