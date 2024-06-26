package com.onlinebookstore.onlinebookstore.mapper;

import com.onlinebookstore.onlinebookstore.config.MapperConfig;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryRequestDto;
import com.onlinebookstore.onlinebookstore.dto.category.CategoryResponseDto;
import com.onlinebookstore.onlinebookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category toModel(CategoryRequestDto requestDto);

    CategoryResponseDto toDto(Category category);

    void updateFromDto(CategoryRequestDto updateDto, @MappingTarget Category category);
}
