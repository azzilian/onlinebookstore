package com.onlinebookstore.onlinebookstore.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookRequestDto {
    @NotNull
    @Size(min = 1)
    private String title;
    @NotNull
    @Size(min = 1)
    private String author;
    @NotNull
    @Size(min = 1)
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
