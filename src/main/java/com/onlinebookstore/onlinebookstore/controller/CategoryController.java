package com.onlinebookstore.onlinebookstore.controller;

import com.onlinebookstore.onlinebookstore.dto.book.BookDtoWithoutCategoriesIds;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryRequestDto;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryResponseDto;
import com.onlinebookstore.onlinebookstore.model.User;
import com.onlinebookstore.onlinebookstore.service.interfaces.BookService;
import com.onlinebookstore.onlinebookstore.service.interfaces.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product management", description = "Endpoints for managing books categories")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/categories")
public class CategoryController {
    private CategoryService categoryService;
    private BookService bookService;

    @GetMapping
    @Operation(summary = "Find All Categories", description = "Find All existing Categories in DB")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<CategoryResponseDto> getAllCategories(Authentication authentication,
                                                      Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return categoryService.findAll(user.getEmail(), pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find Category by id",
            description = "Find specific Category by id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Find Books by Category id",
            description = "Find books with specific Category id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<BookDtoWithoutCategoriesIds> getBooksByCategoryId(@PathVariable Long id,
                                                                  Pageable pageable) {
        return categoryService.getBooksByCategoryId(id, pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new Category",
            description = "Create new Category in DB")
    public CategoryResponseDto createCategory(@Valid @RequestBody
                                                  CategoryRequestDto categoryRequestDto) {
        return categoryService.saveCategory(categoryRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Change Category name",
            description = "Change book data by finding book by id")
    public CategoryResponseDto updateCategory(@Valid @PathVariable long id,
                                          @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.updateCategoryById(id, categoryRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Category from DB by id",
            description = "Delete Category from DB by id"
            + " if it present,otherwise exception will be thrown")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }
}