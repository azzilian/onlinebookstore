package com.onlinebookstore.onlinebookstore.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookRequestDto {
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}