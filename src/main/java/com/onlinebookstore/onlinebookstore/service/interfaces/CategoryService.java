package com.onlinebookstore.onlinebookstore.service.interfaces;

import com.onlinebookstore.onlinebookstore.dto.book.BookDtoWithoutCategoriesIds;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryRequestDto;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto getById(Long id);

    List<CategoryResponseDto> findAll(Pageable pageable);

    List<BookDtoWithoutCategoriesIds> getBooksByCategoryId(Long id, Pageable pageable);

    CategoryResponseDto saveCategory(CategoryRequestDto requestDto);

    CategoryResponseDto updateCategoryById(Long id, CategoryRequestDto requestDto);

    void deleteCategoryById(Long id);
}
