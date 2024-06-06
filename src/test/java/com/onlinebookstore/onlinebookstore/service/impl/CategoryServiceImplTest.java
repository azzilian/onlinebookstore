package com.onlinebookstore.onlinebookstore.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
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

    private static final long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "Fiction";
    private static final String CATEGORY_DESCRIPTION = "Fictional books";
    private static final String UPDATED_CATEGORY_NAME = "Science Fiction";
    private static final String UPDATED_CATEGORY_DESCRIPTION = "Science Fictional books";

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryRequestDto requestDto;
    private Category category;
    private Category savedCategory;
    private Category updatedCategory;
    private CategoryResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = new CategoryRequestDto();
        requestDto.setName(CATEGORY_NAME).setDescription(CATEGORY_DESCRIPTION);

        category = new Category();
        category.setName(CATEGORY_NAME);
        category.setDescription(CATEGORY_DESCRIPTION);

        savedCategory = new Category();
        savedCategory.setId(CATEGORY_ID);
        savedCategory.setName(CATEGORY_NAME);
        savedCategory.setDescription(CATEGORY_DESCRIPTION);

        updatedCategory = new Category();
        updatedCategory.setId(CATEGORY_ID);
        updatedCategory.setName(UPDATED_CATEGORY_NAME);
        updatedCategory.setDescription(UPDATED_CATEGORY_DESCRIPTION);

        responseDto = new CategoryResponseDto(CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESCRIPTION);
    }

    @Test
    @DisplayName("Save new Category")
    void save_NewCategory_Success() {
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.save(requestDto);

        verify(categoryRepository).save(category);
        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    @DisplayName("Update existing Category")
    void update_ExistingCategory_Success() {
        CategoryRequestDto updateRequestDto = new CategoryRequestDto();
        updateRequestDto
                .setName(UPDATED_CATEGORY_NAME)
                .setDescription(UPDATED_CATEGORY_DESCRIPTION);

        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(new CategoryResponseDto(
                CATEGORY_ID,
                UPDATED_CATEGORY_NAME,
                UPDATED_CATEGORY_DESCRIPTION));

        CategoryResponseDto result = categoryService.update(CATEGORY_ID, updateRequestDto);

        verify(categoryRepository).findById(CATEGORY_ID);
        verify(categoryRepository).save(category);
        assertThat(result).isEqualTo(new CategoryResponseDto(
                CATEGORY_ID,
                UPDATED_CATEGORY_NAME,
                UPDATED_CATEGORY_DESCRIPTION));
    }

    @Test
    @DisplayName("Delete existing Category")
    void delete_ExistingCategory_Success() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.delete(CATEGORY_ID);

        verify(categoryRepository).findById(CATEGORY_ID);
        verify(categoryRepository).delete(category);
        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    @DisplayName("Get Category by ID")
    void getById_ExistingCategory_Success() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.getById(CATEGORY_ID);

        verify(categoryRepository).findById(CATEGORY_ID);
        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    @DisplayName("Find all Categories with pagination")
    void findAll_Paginated_Success() {
        List<Category> categories = Collections.singletonList(category);
        Page<Category> page = new PageImpl<>(categories);
        Pageable pageable = PageRequest.of(0, 10);

        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        List<CategoryResponseDto> result = categoryService.findAll(pageable);

        verify(categoryRepository).findAll(pageable);
        assertThat(result).isEqualTo(Collections.singletonList(responseDto));
    }
}
