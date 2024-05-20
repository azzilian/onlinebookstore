package com.onlinebookstore.onlinebookstore.service.impl;

import com.onlinebookstore.onlinebookstore.dto.book.BookDtoWithoutCategoriesIds;
import com.onlinebookstore.onlinebookstore.dto.book.BookRequestDto;
import com.onlinebookstore.onlinebookstore.dto.book.BookResponseDto;
import com.onlinebookstore.onlinebookstore.exeption.EntityNotFoundException;
import com.onlinebookstore.onlinebookstore.mapper.BookMapper;
import com.onlinebookstore.onlinebookstore.model.Book;
import com.onlinebookstore.onlinebookstore.model.Category;
import com.onlinebookstore.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.onlinebookstore.repository.CategoryRepository;
import com.onlinebookstore.onlinebookstore.service.interfaces.BookService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponseDto save(BookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        setCategories(book, requestDto.getCategoryIds());
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookResponseDto> findAll(String email, Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAll(pageable);
        if (booksPage.isEmpty()) {
            throw new EntityNotFoundException("Can't find any books");
        }
        List<Book> books = booksPage.getContent();
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookResponseDto findById(Long id) {
        Book book = findByIdOrThrowException(id, Book.class);
        return bookMapper.toDto(book);
    }

    @Override
    public BookResponseDto update(Long id, BookRequestDto requestDto) {
        Book book = findByIdOrThrowException(id, Book.class);
        bookMapper.updateFromDto(requestDto, book);
        setCategories(book, requestDto.getCategoryIds());
        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public void delete(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Book not found");
        }
    }

    @Override
    public List<BookDtoWithoutCategoriesIds> getBooksById(Long id, Pageable pageable) {
        Category category = findByIdOrThrowException(id, Category.class);
        List<BookDtoWithoutCategoriesIds> books = bookRepository
                .findAllByCategoriesId(id, pageable).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .collect(Collectors.toList());
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Can't find any books for category id " + id);
        }
        return books;
    }

    private <T> T findByIdOrThrowException(Long id, Class<T> entityClass) {
        if (entityClass.equals(Book.class)) {
            return (T) bookRepository.findById(id)
                    .orElseThrow(()
                            -> new EntityNotFoundException("Can't find book by id " + id));
        } else if (entityClass.equals(Category.class)) {
            return (T) categoryRepository.findById(id)
                    .orElseThrow(()
                            -> new EntityNotFoundException("Can't find category by id " + id));
        } else {
            throw new IllegalArgumentException("Unsupported entity class: "
                    + entityClass.getName());
        }
    }

    private void setCategories(Book book, Set<Long> categoryIds) {
        if (categoryIds != null && !categoryIds.isEmpty()) {
            Set<Category> categories = categoryIds.stream()
                    .map(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(()
                                    -> new EntityNotFoundException("Category not found with id "
                                    + categoryId)))
                    .collect(Collectors.toSet());
            book.setCategories(categories);
        } else {
            book.setCategories(null);
        }
    }
}
