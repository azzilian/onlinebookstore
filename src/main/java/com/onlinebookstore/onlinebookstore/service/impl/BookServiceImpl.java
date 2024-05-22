package com.onlinebookstore.onlinebookstore.service.impl;

import com.onlinebookstore.onlinebookstore.dto.book.BookDtoWithoutCategoriesIds;
import com.onlinebookstore.onlinebookstore.dto.book.BookRequestDto;
import com.onlinebookstore.onlinebookstore.dto.book.BookResponseDto;
import com.onlinebookstore.onlinebookstore.exeption.EntityNotFoundException;
import com.onlinebookstore.onlinebookstore.exeption.InvalidCategoryIdException;
import com.onlinebookstore.onlinebookstore.mapper.BookMapper;
import com.onlinebookstore.onlinebookstore.model.Book;
import com.onlinebookstore.onlinebookstore.model.Category;
import com.onlinebookstore.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.onlinebookstore.repository.CategoryRepository;
import com.onlinebookstore.onlinebookstore.service.interfaces.BookService;
import com.onlinebookstore.onlinebookstore.service.interfaces.CategoryService;
import java.util.HashSet;
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
    private final CategoryService categoryService;

    @Override
    public BookResponseDto save(BookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        setCategories(book, requestDto.getCategoryIds());
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDtoWithoutCategoriesIds> findAll(Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAll(pageable);
        if (booksPage.isEmpty()) {
            throw new EntityNotFoundException("Can't find any books");
        }
        List<Book> books = booksPage.getContent();
        return books.stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    @Override
    public BookResponseDto findById(Long id) {
        Book book = findByIdOrThrowException(id);
        return bookMapper.toDto(book);
    }

    @Override
    public BookResponseDto update(Long id, BookRequestDto requestDto) {
        Book book = findByIdOrThrowException(id);
        bookMapper.updateFromDto(requestDto, book);
        setCategories(book, requestDto.getCategoryIds());
        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public void delete(Long id) {
        findByIdOrThrowException(id);
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoriesIds> getBooksById(Long id, Pageable pageable) {
        Category category = categoryService.findByIdOrThrowException(id);
        List<BookDtoWithoutCategoriesIds> books = bookRepository
                .findAllByCategoriesId(id, pageable).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .collect(Collectors.toList());
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Can't find any books for category id " + id);
        }
        return books;
    }

    private Book findByIdOrThrowException(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find category by id " + id));
    }

    private void setCategories(Book book, Set<Long> categoryIds) {
        Set<Category> categorySet = new HashSet<>(categoryRepository.findAllById(categoryIds));
        if (categorySet.size() != categoryIds.size()) {
            Set<Long> foundCategoryIds = categorySet.stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet());
            Set<Long> invalidCategoryIds = new HashSet<>(categoryIds);
            invalidCategoryIds.removeAll(foundCategoryIds);
            throw new InvalidCategoryIdException("Invalid category IDs: " + invalidCategoryIds);
        }
        book.setCategories(categorySet);
    }
}
