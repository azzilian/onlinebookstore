package com.onlinebookstore.onlinebookstore.service.interfaces;

import com.onlinebookstore.onlinebookstore.dto.book.BookDtoWithoutCategoriesIds;
import com.onlinebookstore.onlinebookstore.dto.book.BookRequestDto;
import com.onlinebookstore.onlinebookstore.dto.book.BookResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(BookRequestDto requestDto);

    BookResponseDto update(Long id, BookRequestDto requestDto);

    void delete(Long id);

    List<BookDtoWithoutCategoriesIds> findAll(Pageable pageable);

    BookResponseDto findById(Long id);

    List<BookDtoWithoutCategoriesIds> getBooksByCategoryId(Long id, Pageable pageable);
}
