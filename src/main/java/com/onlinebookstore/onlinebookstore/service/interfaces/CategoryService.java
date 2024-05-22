package com.onlinebookstore.onlinebookstore.service.interfaces;

import com.onlinebookstore.onlinebookstore.dto.category.CategoryRequestDto;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryResponseDto getById(Long id);

    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto save(CategoryRequestDto requestDto);

    CategoryResponseDto update(Long id, CategoryRequestDto requestDto);

    CategoryResponseDto delete(Long id);
}
