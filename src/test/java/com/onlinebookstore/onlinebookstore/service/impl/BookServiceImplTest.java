package com.onlinebookstore.onlinebookstore.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onlinebookstore.onlinebookstore.dto.book.BookDtoWithoutCategoriesIds;
import com.onlinebookstore.onlinebookstore.dto.book.BookRequestDto;
import com.onlinebookstore.onlinebookstore.dto.book.BookResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    private static final String EXPECTED_MESSAGE = "Can't find any books";
    private static final BigDecimal PRICE = BigDecimal.valueOf(10.50);
    private static final String COVER_IMAGE = "test@test.com";
    private static final String BOOK_TITLE_NEW = "Lenore";
    private static final String AUTHOR = "EdgarAllanPoe";
    private static final String BOOK_TITLE_1 = "Book 1";
    private static final String BOOK_TITLE_2 = "Book 2";
    private static final String ISBN = "9780306406157";
    private static final String DESCRIPTION = "test";
    private static final long BOOK_ID_1 = 1L;
    private static final long BOOK_ID_2 = 2L;
    private static final long CATEGORY_ID = 1L;

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

    private Book book1;
    private Book book2;
    private BookRequestDto bookRequestDto;
    private BookResponseDto bookResponseDto;
    private BookDtoWithoutCategoriesIds bookDto1;
    private BookDtoWithoutCategoriesIds bookDto2;
    private Set<Long> categoryIds;
    private List<Category> categories;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        book1 = new Book();
        book1.setId(BOOK_ID_1);
        book1.setTitle(BOOK_TITLE_1);

        book2 = new Book();
        book2.setId(BOOK_ID_2);
        book2.setTitle(BOOK_TITLE_2);

        bookDto1 = new BookDtoWithoutCategoriesIds();
        bookDto1.setId(BOOK_ID_1);
        bookDto1.setTitle(BOOK_TITLE_1);

        bookDto2 = new BookDtoWithoutCategoriesIds();
        bookDto2.setId(BOOK_ID_2);
        bookDto2.setTitle(BOOK_TITLE_2);

        bookRequestDto = new BookRequestDto();
        bookRequestDto.setTitle(BOOK_TITLE_NEW)
                .setAuthor(AUTHOR)
                .setIsbn(ISBN)
                .setPrice(PRICE)
                .setDescription(DESCRIPTION)
                .setCoverImage(COVER_IMAGE)
                .setCategoryIds(new HashSet<>(Collections.singletonList(CATEGORY_ID)));

        bookResponseDto = new BookResponseDto();
        bookResponseDto.setTitle(BOOK_TITLE_NEW);

        categoryIds = new HashSet<>(Collections.singletonList(CATEGORY_ID));
        categories = categoryIds.stream().map(id -> {
            Category category = new Category();
            category.setId(id);
            return category;
        }).collect(Collectors.toList());

        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Find all books in database")
    void findAllBooks_ValidData_OK() {
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book1, book2));

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        Mockito.when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(bookDto1);
        Mockito.when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(bookDto2);

        List<BookDtoWithoutCategoriesIds> result = bookService.findAll(pageable);

        List<BookDtoWithoutCategoriesIds> expectedDtos = Arrays.asList(bookDto1, bookDto2);
        assertThat(result).isEqualTo(expectedDtos);
    }

    @Test
    @DisplayName("Find all books in empty database should throw exception")
    void findAllBooks_EmptyData_NotOk() {
        Page<Book> emptyBookPage = new PageImpl<>(Collections.emptyList());

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(emptyBookPage);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.findAll(pageable);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).contains(EXPECTED_MESSAGE);
    }

    @Test
    @DisplayName("Create new Book")
    void createNewBook_ValidData_OK() {
        Mockito.when(categoryRepository.findAllById(categoryIds)).thenReturn(categories);
        Mockito.when(bookMapper.toModel(bookRequestDto)).thenReturn(book1);
        Mockito.when(bookRepository.save(book1)).thenReturn(book1);
        Mockito.when(bookMapper.toDto(book1)).thenReturn(bookResponseDto);

        BookResponseDto savedBookDto = bookService.save(bookRequestDto);

        Mockito.verify(bookRepository).save(book1);
        Set<Category> categorySet = new HashSet<>(categories);
        assertThat(book1.getCategories()).isEqualTo(categorySet);
        assertThat(savedBookDto).isEqualTo(bookResponseDto);
    }

    @Test
    @DisplayName("Update existing Book")
    void changeExistingBook_ValidData_OK() {
        bookRequestDto.setTitle(BOOK_TITLE_2);
        bookResponseDto.setTitle(BOOK_TITLE_2);

        Mockito.when(bookRepository.findById(BOOK_ID_1)).thenReturn(Optional.of(book1));
        Mockito.when(categoryRepository.findAllById(categoryIds)).thenReturn(categories);
        Mockito.when(bookRepository.save(book1)).thenReturn(book1);
        Mockito.when(bookMapper.toDto(book1)).thenReturn(bookResponseDto);

        BookResponseDto updatedBookDto = bookService.update(BOOK_ID_1, bookRequestDto);

        Set<Category> categorySet = new HashSet<>(categories);
        assertThat(book1.getCategories()).isEqualTo(categorySet);
        assertThat(updatedBookDto).isEqualTo(bookResponseDto);
    }

    @Test
    @DisplayName("Find book by book id")
    void findBookById_ValidData_OK() {
        Mockito.when(bookRepository.findById(BOOK_ID_1)).thenReturn(Optional.of(book1));
        Mockito.when(bookMapper.toDto(book1)).thenReturn(bookResponseDto);

        BookResponseDto foundBook = bookService.findById(BOOK_ID_1);

        assertThat(foundBook).isEqualTo(bookResponseDto);
    }

    @Test
    @DisplayName("Delete book by id")
    void deleteBookById_ValidData_OK() {
        Mockito.when(bookRepository.findById(BOOK_ID_1)).thenReturn(Optional.of(book1));
        bookService.delete(BOOK_ID_1);
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(BOOK_ID_1);
    }

    @Test
    @DisplayName("Find all book by category id")
    void getBooksByCategoryId_ValidData_OK() {
        List<Book> books = Arrays.asList(book1, book2);

        Mockito.when(bookRepository.findAllByCategoriesId(CATEGORY_ID, pageable)).thenReturn(books);
        Mockito.when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(bookDto1);
        Mockito.when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(bookDto2);

        List<BookDtoWithoutCategoriesIds> result = bookService
                .getBooksByCategoryId(CATEGORY_ID, pageable);
        List<BookDtoWithoutCategoriesIds> expectedDtos = Arrays.asList(bookDto1, bookDto2);
        assertThat(result).isEqualTo(expectedDtos);
    }
}
