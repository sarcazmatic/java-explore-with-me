package ru.practicum.api.all.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.all.service.PublicCategoriesService;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.utility.PageableMaker;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoriesController {

    private final PublicCategoriesService publicCategoriesService;

    @GetMapping
    public List<CategoryDto> getCategory(@RequestParam(required = false, defaultValue = "0") int from,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        return publicCategoriesService.getCategory(PageableMaker.makePageable(from, size));
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable @NotNull long catId) {
        return publicCategoriesService.getCategoryById(catId);
    }

}
