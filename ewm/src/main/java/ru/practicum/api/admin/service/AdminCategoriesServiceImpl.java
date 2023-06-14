package ru.practicum.api.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCategoriesServiceImpl implements AdminCategoriesService{

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.fromNewCategoryDto(newCategoryDto)));
    }

    @Override
    public CategoryDto patchCategory(long catId, NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.findCategoryById(catId);
        category.setName(newCategoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(long catId) {
        categoryRepository.deleteById(catId);
    }
}
