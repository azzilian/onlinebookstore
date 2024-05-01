package com.onlinebookstore.onlinebookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookRequestDto {
    @NotNull(message = "title field cannot be empty")
    @Size(min = 1, message = "title name should be at least 1 character")
    private String title;
    @NotNull(message = "author field cannot be empty")
    @Size(min = 1, message = "author name should be at least 1 character")
    private String author;
    @NotNull(message = "ISBN field cannot be empty")
    @Pattern(regexp = "^(?:ISBN(?:-13)?:? )"
            + "?(?=[-0-9]{17}$|[-0-9X]{13}$|[-0-9]{10}$|(?=(?:[-0-9]{2,6})"
            + "?[-0-9]{1,5}-|)[- 0-9X]{1,17}$)97[89][- ]?[0-9]+[- ]"
            + "?[0-9]+[- ]?[0-9]+[- ]?[0-9]$", message = "Invalid ISBN format, tip use ISBN-13")
    private String isbn;
    @NotNull(message = "price field cannot be empty")
    @Min(value = 0, message = "price cannot be lover than 0")
    private BigDecimal price;
    private String description;
    private String coverImage;
}
