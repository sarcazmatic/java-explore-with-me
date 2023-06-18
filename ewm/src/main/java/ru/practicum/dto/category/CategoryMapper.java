package ru.practicum.dto.category;

import ru.practicum.model.Category;

public class CategoryMapper {

    public static Category fromNewCategoryDto(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public static Category fromCategoryDto(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .id(categoryDto.getId())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
