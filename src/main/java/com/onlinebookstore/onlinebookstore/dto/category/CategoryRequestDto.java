package com.onlinebookstore.onlinebookstore.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotNull(message = "name field cannot be empty")
    @Size(min = 1, message = "Category name should be at least 1 character")
    private String name;
    private String description;
}
