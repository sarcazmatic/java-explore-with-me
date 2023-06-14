package ru.practicum.dto.category;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class NewCategoryDto {

    @NotBlank
    @Size(max = 50)
    private String name;
}
