package com.onlinebookstore.onlinebookstore.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryRequestDto {
    @NotNull(message = "name field cannot be empty")
    @Size(min = 1,
            max = 255,
            message = "Category name should be at least 1 character")
    private String name;
    @Size(max = 510,
            message = "Category description cannot be more than 510 character")
    private String description;
}
