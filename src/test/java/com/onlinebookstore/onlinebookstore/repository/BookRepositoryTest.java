package com.onlinebookstore.onlinebookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.onlinebookstore.onlinebookstore.model.Book;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find 2 Books with same category by categoryId")
    @Sql(scripts = {
            "classpath:database/book/remove-books_categories-from-books_categories-table.sql",
            "classpath:database/book/remove-categories-from-categories-table.sql",
            "classpath:database/book/remove-books-from-books-table.sql",
            "classpath:database/book/add-books-to-books-table.sql",
            "classpath:database/book/add-categories-to-categories-table.sql",
            "classpath:database/book/add-books_categories-to-books_categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-books_categories-from-books_categories-table.sql",
            "classpath:database/book/remove-categories-from-categories-table.sql",
            "classpath:database/book/remove-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllBooksByCategoriesId() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        List<Book> books = bookRepository.findAllByCategoriesId(categoryId, pageable);

        assertEquals(2, books.size());
    }
}
