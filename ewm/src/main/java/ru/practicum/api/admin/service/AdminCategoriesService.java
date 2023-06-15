package ru.practicum.api.admin.service;


import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

public interface AdminCategoriesService {

    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    CategoryDto patchCategory(long catId, NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);

}
