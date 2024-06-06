package com.onlinebookstore.onlinebookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.onlinebookstore.onlinebookstore.dto.book.BookDtoWithoutCategoriesIds;
import com.onlinebookstore.onlinebookstore.dto.book.BookRequestDto;
import com.onlinebookstore.onlinebookstore.dto.book.BookResponseDto;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryResponseDto;
import com.onlinebookstore.onlinebookstore.exeption.EntityNotFoundException;
import com.onlinebookstore.onlinebookstore.mapper.BookMapper;
import com.onlinebookstore.onlinebookstore.model.Book;
import com.onlinebookstore.onlinebookstore.model.Category;
import com.onlinebookstore.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.onlinebookstore.repository.CategoryRepository;
import com.onlinebookstore.onlinebookstore.service.interfaces.CategoryService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

@ExtendWith(MockitoExtension.class)
@Sql(scripts = {
        "classpath:database/book/remove-all-from-all-tables.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Find all books in database")
    @Sql(scripts = {
            "classpath:database/book/remove-all-from-all-tables.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllBooks_ValidData_OK() {

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");

        BookDtoWithoutCategoriesIds dto1 = new BookDtoWithoutCategoriesIds();
        dto1.setId(1L);
        dto1.setTitle("Dragon Lance");

        BookDtoWithoutCategoriesIds dto2 = new BookDtoWithoutCategoriesIds();
        dto2.setId(2L);
        dto2.setTitle("Dragon Lance 2");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book1, book2));

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        Mockito.when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(dto1);
        Mockito.when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(dto2);

        List<BookDtoWithoutCategoriesIds> result = bookService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    @DisplayName("Find all books in empty database should throw exception")
    @Sql(scripts = {
            "classpath:database/book/remove-all-from-all-tables.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllBooks_EmptyData_NotOk() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> emptyBookPage = new PageImpl<>(Collections.emptyList());

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(emptyBookPage);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.findAll(pageable);
        });

        String expectedMessage = "Can't find any books";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Create new Book")
    @Sql(scripts = {
            "classpath:database/book/remove-all-from-all-tables.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createNewBook_ValidData_OK() {
        Set<Long> categoryIds = new HashSet<>();
        categoryIds.add(1L);

        BookRequestDto dto = new BookRequestDto();
        dto.setTitle("Lenore")
                .setAuthor("EdgarAllanPoe")
                .setIsbn("9780306406157")
                .setPrice(BigDecimal.valueOf(10.50))
                .setDescription("test")
                .setCoverImage("test@test.com")
                .setCategoryIds(categoryIds);

        Book book = new Book();
        book.setTitle("Lenore")
                .setAuthor("EdgarAllanPoe")
                .setIsbn("9780306406157")
                .setPrice(BigDecimal.valueOf(10.50))
                .setDescription("test")
                .setCoverImage("test@test.com");

        List<Category> categories = categoryIds.stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toList());

        BookDtoWithoutCategoriesIds bookDtoWithoutCategories = new BookDtoWithoutCategoriesIds();
        bookDtoWithoutCategories.setTitle("Lenore");

        BookResponseDto bookResponseDto = new BookResponseDto();
        bookResponseDto.setTitle("Lenore");

        Mockito.when(categoryRepository.findAllById(categoryIds)).thenReturn(categories);
        Mockito.when(bookMapper.toModel(dto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookResponseDto);

        BookResponseDto savedBookDto = bookService.save(dto);

        Mockito.verify(bookRepository).save(book);
        Set<Category> categorySet = new HashSet<>(categories);
        assertEquals(categorySet, book.getCategories());

        assertNotNull(savedBookDto);
        assertEquals(dto.getTitle(), savedBookDto.getTitle());
    }

    @Test
    @DisplayName("Update existing Book")
    @Sql(scripts = {
            "classpath:database/book/remove-all-from-all-tables.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changeExistingBook_ValidData_OK() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book 1");

        BookRequestDto requestDto = new BookRequestDto();
        requestDto.setTitle("Book 2");
        Set<Long> categoryIds = new HashSet<>();
        categoryIds.add(1L);
        requestDto.setCategoryIds(categoryIds);

        BookResponseDto bookResponseDto = new BookResponseDto();
        bookResponseDto.setId(1L);
        bookResponseDto.setTitle("Book 2");

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        List<Category> categories = categoryIds.stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toList());
        Mockito.when(categoryRepository.findAllById(categoryIds)).thenReturn(categories);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookResponseDto);

        BookResponseDto updatedBookDto = bookService.update(1L, requestDto);

        Set<Category> categorySet = new HashSet<>(categories);
        assertEquals(categorySet, book.getCategories());

        assertNotNull(updatedBookDto);
        assertEquals(requestDto.getTitle(), updatedBookDto.getTitle());
    }

    @Test
    @DisplayName("Find book by book id")
    @Sql(scripts = {
            "classpath:database/book/remove-all-from-all-tables.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findBookById_ValidData_OK() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book 1");

        BookResponseDto dto = new BookResponseDto();
        dto.setId(1L);
        dto.setTitle("Book 1");

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(dto);

        BookResponseDto foundBook = bookService.findById(1L);

        assertNotNull(foundBook);
        assertEquals(dto.getId(), foundBook.getId());
        assertEquals(dto.getTitle(), foundBook.getTitle());
    }

    @Test
    @DisplayName("Delete book by id")
    @Sql(scripts = {
            "classpath:database/book/remove-all-from-all-tables.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBookById_ValidData_OK() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book 1");

        BookResponseDto dto = new BookResponseDto();
        dto.setId(1L);
        dto.setTitle("Book 1");

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        bookService.delete(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Find all book by category id")
    @Sql(scripts = {
            "classpath:database/book/remove-all-from-all-tables.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooksByCategoryId_ValidData_OK() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");

        BookDtoWithoutCategoriesIds dto1 = new BookDtoWithoutCategoriesIds();
        dto1.setId(1L);
        dto1.setTitle("Book 1");

        BookDtoWithoutCategoriesIds dto2 = new BookDtoWithoutCategoriesIds();
        dto2.setId(2L);
        dto2.setTitle("Book 2");

        List<Book> books = Arrays.asList(book1, book2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book1, book2));
        Long categoryId = 1L;
        Mockito.when(bookRepository.findAllByCategoriesId(categoryId,pageable))
                .thenReturn(books);
        Mockito.when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(dto1);
        Mockito.when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(dto2);

        List<BookDtoWithoutCategoriesIds> result = bookService
                .getBooksByCategoryId(categoryId, pageable);
        List<BookDtoWithoutCategoriesIds> dtos = Arrays.asList(dto1, dto2);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dtos.get(0), result.get(0));
        assertEquals(dtos.get(1), result.get(1));
    }
}
