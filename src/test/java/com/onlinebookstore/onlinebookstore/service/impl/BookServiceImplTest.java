package com.onlinebookstore.onlinebookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.onlinebookstore.onlinebookstore.dto.book.BookDtoWithoutCategoriesIds;
import com.onlinebookstore.onlinebookstore.exeption.EntityNotFoundException;
import com.onlinebookstore.onlinebookstore.mapper.BookMapper;
import com.onlinebookstore.onlinebookstore.model.Book;
import com.onlinebookstore.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.onlinebookstore.repository.CategoryRepository;
import com.onlinebookstore.onlinebookstore.service.interfaces.CategoryService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
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
    void findAll_ShouldReturnAllBooks() {

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
    void findAll_ShouldThrowException_WhenNoBooksFound() {
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
}
