package com.onlinebookstore.onlinebookstore.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryRequestDto;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryResponseDto;
import com.onlinebookstore.onlinebookstore.service.interfaces.BookService;
import com.onlinebookstore.onlinebookstore.service.interfaces.CategoryService;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

    @Autowired
    protected static MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private BookService bookService;

    private CategoryResponseDto categoryResponseDto;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext,
            @Autowired DataSource dataSource) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
    }

    @BeforeEach
    void setUp() {
        categoryResponseDto = new CategoryResponseDto(1L, "Fiction", "Fictional books");
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(categoryResponseDto.id().intValue())))
                .andExpect(jsonPath("$[0].name", is(categoryResponseDto.name())))
                .andExpect(jsonPath("$[0].description", is(categoryResponseDto.description())));
    }

    @Test
    @DisplayName("Get category by ID")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getCategoryById_OK() throws Exception {
        Mockito.when(categoryService.getById(anyLong())).thenReturn(categoryResponseDto);

        mockMvc.perform(get("/api/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        is(categoryResponseDto.id().intValue())))
                .andExpect(jsonPath("$.name",
                        is(categoryResponseDto.name())))
                .andExpect(jsonPath("$.description",
                        is(categoryResponseDto.description())));
    }

    @Test
    @DisplayName("Create new category")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategory_OK() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("Fiction")
                .setDescription("Fictional books");

        Mockito.when(categoryService.save(any(CategoryRequestDto.class)))
                .thenReturn(categoryResponseDto);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(categoryResponseDto.id().intValue())))
                .andExpect(jsonPath("$.name", is(categoryResponseDto.name())))
                .andExpect(jsonPath("$.description", is(categoryResponseDto.description())));
    }

    @Test
    @DisplayName("Update category")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateCategory_OK() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("Updated Fiction")
                .setDescription("Updated fictional books");

        CategoryResponseDto updatedCategoryResponseDto = new CategoryResponseDto(1L,
                "Updated Fiction", "Updated fictional books");

        Mockito.when(categoryService.update(anyLong(), any(CategoryRequestDto.class)))
                .thenReturn(updatedCategoryResponseDto);

        mockMvc.perform(put("/api/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedCategoryResponseDto.id().intValue())))
                .andExpect(jsonPath("$.name", is(updatedCategoryResponseDto.name())))
                .andExpect(jsonPath("$.description", is(updatedCategoryResponseDto.description())));
    }

    @Test
    @DisplayName("Delete category")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteCategory_OK() throws Exception {
        mockMvc.perform(delete("/api/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryService).delete(1L);
        verifyNoMoreInteractions(categoryService);
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    private static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/remove-all-from-books-table.sql")
            );
        }
    }
}
