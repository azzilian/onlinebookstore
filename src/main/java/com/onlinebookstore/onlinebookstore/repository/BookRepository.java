package com.onlinebookstore.onlinebookstore.repository;

import com.onlinebookstore.onlinebookstore.model.Book;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByCategoryId(Long categoryId, Pageable pageable);
}
