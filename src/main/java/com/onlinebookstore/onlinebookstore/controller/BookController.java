package com.onlinebookstore.onlinebookstore.controller;

import com.onlinebookstore.onlinebookstore.dto.book.BookDto;
import com.onlinebookstore.onlinebookstore.dto.book.BookRequestDto;
import com.onlinebookstore.onlinebookstore.model.User;
import com.onlinebookstore.onlinebookstore.service.interfaces.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product management", description = "Endpoints for managing products")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/books")
public class BookController {

    private final BookService bookService;
    private Authentication authentication;

    @GetMapping
    @Operation(summary = "Find All Books", description = "Find All Books in DB")
    @PreAuthorize("hasRole('USER')")
    public List<BookDto> getAll(Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return bookService.findAll(user.getEmail(), pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find Book by id", description = "Find Book by id")
    @PreAuthorize("hasRole('USER')")
    public BookDto findById(@PathVariable Long id) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        return bookService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new Book", description = "Create new Book in DB")
    public BookDto createBook(@Valid @RequestBody BookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Change Book data", description = "Change book data by finding book by id")
    public BookDto updateBook(@Valid @PathVariable long id, @RequestBody BookRequestDto bookDto) {
        return bookService.update(id, bookDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Book from DB by id", description = "Delete book from DB by id"
            + " if it present,otherwise exception will be thrown")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
