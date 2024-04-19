package com.onlinebookstore.onlinebookstore.service;

import com.onlinebookstore.onlinebookstore.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
