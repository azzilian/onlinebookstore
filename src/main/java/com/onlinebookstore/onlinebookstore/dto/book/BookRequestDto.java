package com.onlinebookstore.onlinebookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.ISBN;

@Data
@Accessors(chain = true)
public class BookRequestDto {
    @NotNull(message = "title field cannot be empty")
    @Size(min = 1, message = "title name should be at least 1 character")
    private String title;
    @NotNull(message = "author field cannot be empty")
    @Size(min = 1, message = "author name should be at least 1 character")
    private String author;
    @NotNull(message = "ISBN field cannot be empty")
    @ISBN(message = "Invalid ISBN format")
    private String isbn;
    @NotNull(message = "price field cannot be empty")
    @Min(value = 0, message = "price cannot be lover than 0")
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Long> categoryIds;
}
