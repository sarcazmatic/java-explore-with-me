package ru.practicum.dto.category;

import ru.practicum.model.Category;

import javax.validation.constraints.NotNull;

public class CategoryMapper {

    public static Category fromNewCategoryDto(@NotNull NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
