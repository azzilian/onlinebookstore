package com.onlinebookstore.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.onlinebookstore.dto.book.BookRequestDto;
import com.onlinebookstore.onlinebookstore.dto.book.BookResponseDto;
import com.onlinebookstore.onlinebookstore.service.interfaces.BookService;
import com.onlinebookstore.onlinebookstore.utils.TearDownDatabase;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(10.50);
    private static final String BOOK_COVER_IMAGE = "test@test.com";
    private static final String BOOK_AUTHOR = "EdgarAllanPoe";
    private static final String BOOK_ISBN = "9780306406157";
    private static final String BOOK_DESCRIPTION = "test";
    private static final String BOOK_TITLE = "Lenore";
    private static final long BOOK_ID = 1L;

    private static TearDownDatabase tearDownDatabase;

    @Autowired
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BookRequestDto requestDto;
    private BookResponseDto expectedDto;

    @MockBean
    private BookService bookService;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext,
            @Autowired DataSource dataSource) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        tearDownDatabase.teardown(dataSource);
    }

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) {
        TearDownDatabase.teardown(dataSource);
        Set<Long> categoryIds = new HashSet<>();
        categoryIds.add(1L);

        requestDto = new BookRequestDto()
                .setTitle(BOOK_TITLE)
                .setAuthor(BOOK_AUTHOR)
                .setIsbn(BOOK_ISBN)
                .setPrice(BOOK_PRICE)
                .setDescription(BOOK_DESCRIPTION)
                .setCoverImage(BOOK_COVER_IMAGE)
                .setCategoryIds(categoryIds);

        expectedDto = new BookResponseDto()
                .setId(BOOK_ID)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoryIds(requestDto.getCategoryIds());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create new book")
    void createBook_ValidRequestDto_OK() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        when(bookService.save(any(BookRequestDto.class))).thenReturn(expectedDto);

        MvcResult result = mockMvc.perform(post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();

        BookResponseDto actualDto = objectMapper.readValue(jsonResponse, BookResponseDto.class);
        Assertions.assertNotNull(actualDto);
        Assertions.assertNotNull(actualDto.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id"));
    }

    @Test
    @DisplayName("Find Book by Id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findBookById_ValidData_OK() throws Exception {
        when(bookService.findById(BOOK_ID)).thenReturn(expectedDto);

        MvcResult result = mockMvc.perform(get("/api/books/{id}", BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedDto)))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BookResponseDto actualDto = objectMapper.readValue(jsonResponse, BookResponseDto.class);
        Assertions.assertNotNull(actualDto);
        assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id"));
    }

    @Test
    @DisplayName("Update Book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateBookById_ValidData_OK() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        when(bookService.update(any(Long.class),
                any(BookRequestDto.class))).thenReturn(expectedDto);

        MvcResult result = mockMvc.perform(put("/api/books/{id}", BOOK_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedDto)))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BookResponseDto actualDto = objectMapper.readValue(jsonResponse, BookResponseDto.class);
        Assertions.assertNotNull(actualDto);
        assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id"));
    }

    @Test
    @DisplayName("Delete Book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteBookById_ValidData_OK() throws Exception {
        MvcResult result = mockMvc.perform(delete("/api/books/{id}", BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(bookService).delete(BOOK_ID);
        verifyNoMoreInteractions(bookService);
    }
}
