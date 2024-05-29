package com.onlinebookstore.onlinebookstore.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.onlinebookstore.dto.book.BookRequestDto;
import com.onlinebookstore.onlinebookstore.dto.book.BookResponseDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;
import javax.sql.DataSource;

import com.onlinebookstore.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.onlinebookstore.service.interfaces.BookService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BookRequestDto requestDto;

    private BookService bookService;

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
        Set<Long> categoryIds = new HashSet<>();
        categoryIds.add(1L);

        requestDto = new BookRequestDto()
                .setTitle("Lenore")
                .setAuthor("EdgarAllanPoe")
                .setIsbn("9780306406157")
                .setPrice(BigDecimal.valueOf(10.50))
                .setDescription("test")
                .setCoverImage("test@test.com")
                .setCategoryIds(categoryIds);
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create new book")
    @Sql(scripts = "classpath:database/book/remove-lenore-from-books-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_OK() throws Exception {

        BookResponseDto expected = new BookResponseDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoryIds(requestDto.getCategoryIds());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BookResponseDto actual = objectMapper
                .readValue(result
                        .getResponse()
                        .getContentAsString(), BookResponseDto.class);

        EqualsBuilder.reflectionEquals(expected,actual,"id");
    }

    @Test
    @DisplayName("Get All Books")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllBooks() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("Find Book by Id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findBookById() throws Exception {
        Long id = 1L;
        mockMvc.perform(get("/api/books/{id}", id))
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("Update Book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateBook() throws Exception {
        Long id = 1L;
        BookRequestDto requestDto = new BookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(put("/api/books/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("Delete Book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteBook() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(bookService).delete(id);
        verifyNoMoreInteractions(bookService);
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
