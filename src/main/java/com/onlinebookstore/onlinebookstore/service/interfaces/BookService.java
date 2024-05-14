package com.onlinebookstore.onlinebookstore.service.interfaces;

import com.onlinebookstore.onlinebookstore.dto.book.BookDto;
import com.onlinebookstore.onlinebookstore.dto.book.BookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(BookRequestDto requestDto);

    BookDto update(Long id, BookRequestDto requestDto);

    BookDto delete(Long id);

    List<BookDto> findAll(String email, Pageable pageable);

    BookDto findById(Long id);
}
