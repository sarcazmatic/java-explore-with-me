package ru.practicum.api.all.service;


import org.springframework.data.domain.Pageable;
import ru.practicum.dto.category.CategoryDto;

import java.util.List;

public interface PublicCategoriesService {

    List<CategoryDto> getCategory(Pageable pageable);

    CategoryDto getCategoryById(long catId);

}
