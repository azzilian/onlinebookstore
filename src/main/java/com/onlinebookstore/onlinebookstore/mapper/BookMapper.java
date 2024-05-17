package com.onlinebookstore.onlinebookstore.mapper;

import com.onlinebookstore.onlinebookstore.config.MapperConfig;
import com.onlinebookstore.onlinebookstore.dto.book.BookDtoWithoutCategoriesIds;
import com.onlinebookstore.onlinebookstore.dto.book.BookRequestDto;
import com.onlinebookstore.onlinebookstore.dto.book.BookResponseDto;
import com.onlinebookstore.onlinebookstore.model.Book;
import com.onlinebookstore.onlinebookstore.model.Category;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "id", ignore = true)
    Book toModel(BookRequestDto requestDto);

    BookResponseDto toDto(Book book);

    void updateFromDto(BookRequestDto updateDto, @MappingTarget Book book);

    BookDtoWithoutCategoriesIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookResponseDto bookResponseDto, Book book) {
        if (book.getCategories() != null) {
            bookResponseDto.setCategoryIds(book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
        }
    }
}
