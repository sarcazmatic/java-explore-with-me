package ru.practicum.api.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.admin.service.AdminCategoriesService;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    private final AdminCategoriesService adminCategoriesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto postCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return adminCategoriesService.postCategory(newCategoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable long catId, @RequestBody @Valid NewCategoryDto newCategoryDto) {
        return adminCategoriesService.patchCategory(catId, newCategoryDto);
    }

    @DeleteMapping ("/{catId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        adminCategoriesService.deleteCategory(catId);
    }

}
