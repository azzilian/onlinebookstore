package com.onlinebookstore.onlinebookstore.service;

import com.onlinebookstore.onlinebookstore.dto.BookDto;
import com.onlinebookstore.onlinebookstore.dto.BookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(BookRequestDto requestDto);

    BookDto update(Long id, BookRequestDto requestDto);

    BookDto delete(Long id);

    List<BookDto> findAll();

    BookDto findById(Long id);
}
