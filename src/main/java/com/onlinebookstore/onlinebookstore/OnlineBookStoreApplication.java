package com.onlinebookstore.onlinebookstore;

import com.onlinebookstore.onlinebookstore.model.Book;
import com.onlinebookstore.onlinebookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) {
                Book book = new Book();
                book.setTitle("curseOfStrahd");
                book.setPrice(BigDecimal.valueOf(111.12));
                book.setAuthor("WizardsOftheCoast");
                book.setIsbn("1233-1234632");

                bookService.save(book);
                System.out.println(bookService.findAll());
            }
        };
    }
}
