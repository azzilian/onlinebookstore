package com.onlinebookstore.onlinebookstore.service.impl;

import com.onlinebookstore.onlinebookstore.dto.book.BookDtoWithoutCategoriesIds;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryRequestDto;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryResponseDto;
import com.onlinebookstore.onlinebookstore.exeption.EntityNotFoundException;
import com.onlinebookstore.onlinebookstore.mapper.BookMapper;
import com.onlinebookstore.onlinebookstore.mapper.CategoryMapper;
import com.onlinebookstore.onlinebookstore.model.Category;
import com.onlinebookstore.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.onlinebookstore.repository.CategoryRepository;
import com.onlinebookstore.onlinebookstore.service.interfaces.CategoryService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;

    @Override
    public CategoryResponseDto getById(Long id) {
        Category category = findByIdOrThrowException(id);
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryResponseDto> findAll(String string, Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        if (categoryPage.isEmpty()) {
            throw new EntityNotFoundException("Can't find any categories");
        }
        List<Category> categories = categoryPage.getContent();
        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDtoWithoutCategoriesIds> getBooksByCategoryId(Long id, Pageable pageable) {
        Category category = findByIdOrThrowException(id);
        List<BookDtoWithoutCategoriesIds> books = bookRepository
                .findAllByCategoriesId(id, pageable).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .collect(Collectors.toList());
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Can't find any books for category id " + id);
        }
        return books;
    }

    @Override
    public CategoryResponseDto saveCategory(CategoryRequestDto requestDto) {
        Category category = categoryMapper.toModel(requestDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryResponseDto updateCategoryById(Long id, CategoryRequestDto requestDto) {
        Category category = findByIdOrThrowException(id);
        categoryMapper.updateFromDto(requestDto, category);
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public CategoryResponseDto deleteCategoryById(Long id) {
        Category category = findByIdOrThrowException(id);
        categoryRepository.delete(category);
        return categoryMapper.toDto(category);
    }

    private Category findByIdOrThrowException(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find category by id " + id));
    }
}
