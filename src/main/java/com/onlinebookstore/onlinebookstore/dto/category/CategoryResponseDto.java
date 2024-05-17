package com.onlinebookstore.onlinebookstore.dto.category;

public record CategoryResponseDto(
    private Long id,
    private String name,
    private String description
) {
}
