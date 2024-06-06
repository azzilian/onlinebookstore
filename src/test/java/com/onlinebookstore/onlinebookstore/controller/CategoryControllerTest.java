package com.onlinebookstore.onlinebookstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryRequestDto;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryResponseDto;
import com.onlinebookstore.onlinebookstore.service.interfaces.BookService;
import com.onlinebookstore.onlinebookstore.service.interfaces.CategoryService;
import com.onlinebookstore.onlinebookstore.utils.TearDownDatabase;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    private static final String UPDATED_CATEGORY_DESCRIPTION = "Updated fictional books";
    private static final String UPDATED_CATEGORY_NAME = "Updated Fiction";
    private static final String CATEGORY_DESCRIPTION = "Fictional books";
    private static final String CATEGORY_NAME = "Fiction";
    private static final long CATEGORY_ID_FIRST = 1L;
    private static TearDownDatabase tearDownDatabase;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private BookService bookService;

    private MockMvc mockMvc;

    private CategoryResponseDto categoryResponseDto;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        categoryResponseDto = new CategoryResponseDto(
                CATEGORY_ID_FIRST,
                CATEGORY_NAME,
                CATEGORY_DESCRIPTION);
        tearDownDatabase.teardown(dataSource);
    }

    @Test
    @DisplayName("Get all categories")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllCategories_OK() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<CategoryResponseDto> categories = Collections.singletonList(categoryResponseDto);

        Mockito.when(categoryService.findAll(pageable)).thenReturn(categories);

        mockMvc.perform(get("/api/categories")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get category by ID")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getCategoryById_OK() throws Exception {
        Mockito.when(categoryService.getById(anyLong())).thenReturn(categoryResponseDto);

        mockMvc.perform(get("/api/categories/{id}",
                        CATEGORY_ID_FIRST)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Create new category")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategory_OK() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName(CATEGORY_NAME)
                .setDescription(CATEGORY_DESCRIPTION);

        Mockito.when(categoryService
                .save(any(CategoryRequestDto.class)))
                .thenReturn(categoryResponseDto);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Update category")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateCategory_OK() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto
                .setName(UPDATED_CATEGORY_NAME)
                .setDescription(UPDATED_CATEGORY_DESCRIPTION);

        CategoryResponseDto updatedCategoryResponseDto = new CategoryResponseDto(
                CATEGORY_ID_FIRST,
                UPDATED_CATEGORY_NAME,
                UPDATED_CATEGORY_DESCRIPTION);

        Mockito.when(categoryService.update(anyLong(),
                any(CategoryRequestDto.class))).thenReturn(updatedCategoryResponseDto);

        mockMvc.perform(put("/api/categories/{id}", CATEGORY_ID_FIRST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete category")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteCategory_OK() throws Exception {
        mockMvc.perform(delete("/api/categories/{id}", CATEGORY_ID_FIRST)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryService).delete(CATEGORY_ID_FIRST);
        verifyNoMoreInteractions(categoryService);
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) {
        tearDownDatabase.teardown(dataSource);
    }
}
