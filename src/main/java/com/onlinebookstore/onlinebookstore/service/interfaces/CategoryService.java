package com.onlinebookstore.onlinebookstore.service.interfaces;

import com.onlinebookstore.onlinebookstore.dto.book.BookDtoWithoutCategoriesIds;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryRequestDto;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryResponseDto getById(Long id);

    List<CategoryResponseDto> findAll(String string, Pageable pageable);

    List<BookDtoWithoutCategoriesIds> getBooksById(Long id, Pageable pageable);

    CategoryResponseDto save(CategoryRequestDto requestDto);

    CategoryResponseDto updateById(Long id, CategoryRequestDto requestDto);

    CategoryResponseDto deleteById(Long id);
}
