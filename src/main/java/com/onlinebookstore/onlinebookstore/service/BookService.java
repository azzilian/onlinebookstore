package com.onlinebookstore.onlinebookstore.service;

import com.onlinebookstore.onlinebookstore.dto.BookDto;
import com.onlinebookstore.onlinebookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    public BookDto getBookById(Long id);
}
