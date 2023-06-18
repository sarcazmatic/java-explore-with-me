package ru.practicum.dto.category;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CategoryDto {

    private long id;
    @NotNull
    private String name;
}
