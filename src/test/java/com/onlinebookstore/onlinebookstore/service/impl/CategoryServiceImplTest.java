package com.onlinebookstore.onlinebookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.onlinebookstore.onlinebookstore.dto.category.CategoryRequestDto;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryResponseDto;
import com.onlinebookstore.onlinebookstore.mapper.CategoryMapper;
import com.onlinebookstore.onlinebookstore.model.Category;
import com.onlinebookstore.onlinebookstore.repository.CategoryRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Save new Category")
    void save_NewCategory_Success() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Fiction")
                .setDescription("Fictional books");

        Category category = new Category();
        category.setName("Fiction");
        category.setDescription("Fictional books");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Fiction");
        savedCategory.setDescription("Fictional books");

        CategoryResponseDto responseDto = new CategoryResponseDto(1L, "Fiction", "Fictional books");

        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.save(requestDto);

        verify(categoryRepository).save(category);
        assertNotNull(result);
        assertEquals(requestDto.getName(), result.name());
        assertEquals(requestDto.getDescription(), result.description());
    }

    @Test
    @DisplayName("Update existing Category")
    void update_ExistingCategory_Success() {
        Long categoryId = 1L;
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Science Fiction")
                .setDescription("Science Fictional books");

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fiction");
        category.setDescription("Fictional books");

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName("Science Fiction");
        updatedCategory.setDescription("Science Fictional books");

        CategoryResponseDto responseDto = new CategoryResponseDto(categoryId, "Science Fiction", "Science Fictional books");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.update(categoryId, requestDto);

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(category);
        assertNotNull(result);
        assertEquals(requestDto.getName(), result.name());
        assertEquals(requestDto.getDescription(), result.description());
    }

    @Test
    @DisplayName("Delete existing Category")
    void delete_ExistingCategory_Success() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fiction");
        category.setDescription("Fictional books");

        CategoryResponseDto responseDto = new CategoryResponseDto(categoryId, "Fiction", "Fictional books");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.delete(categoryId);

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).delete(category);
        assertNotNull(result);
        assertEquals(category.getName(), result.name());
        assertEquals(category.getDescription(), result.description());
    }

    @Test
    @DisplayName("Get Category by ID")
    void getById_ExistingCategory_Success() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fiction");
        category.setDescription("Fictional books");

        CategoryResponseDto responseDto = new CategoryResponseDto(categoryId, "Fiction", "Fictional books");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.getById(categoryId);

        verify(categoryRepository).findById(categoryId);
        assertNotNull(result);
        assertEquals(category.getName(), result.name());
        assertEquals(category.getDescription(), result.description());
    }

    @Test
    @DisplayName("Find all Categories with pagination")
    void findAll_Paginated_Success() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fiction");
        category.setDescription("Fictional books");

        CategoryResponseDto responseDto = new CategoryResponseDto(1L, "Fiction", "Fictional books");

        List<Category> categories = Collections.singletonList(category);
        Page<Category> page = new PageImpl<>(categories);
        Pageable pageable = PageRequest.of(0, 10);

        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        List<CategoryResponseDto> result = categoryService.findAll(pageable);

        verify(categoryRepository).findAll(pageable);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(category.getName(), result.get(0).name());
        assertEquals(category.getDescription(), result.get(0).description());
    }
}
