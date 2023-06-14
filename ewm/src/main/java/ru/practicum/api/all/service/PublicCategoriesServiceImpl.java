package ru.practicum.api.all.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicCategoriesServiceImpl implements PublicCategoriesService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(o -> CategoryMapper.toCategoryDto(o))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не существует!"));

        return CategoryMapper.toCategoryDto(category);
    }

}
