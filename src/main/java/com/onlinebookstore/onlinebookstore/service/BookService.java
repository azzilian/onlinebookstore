package com.onlinebookstore.onlinebookstore.service;

import com.onlinebookstore.onlinebookstore.dto.BookDto;
import com.onlinebookstore.onlinebookstore.dto.CreateBookRequestDto;
import com.onlinebookstore.onlinebookstore.dto.UpdateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto update(Long id, UpdateBookRequestDto requestDto);

    BookDto delete(Long id);

    List<BookDto> findAll();

    BookDto findById(Long id);
}
