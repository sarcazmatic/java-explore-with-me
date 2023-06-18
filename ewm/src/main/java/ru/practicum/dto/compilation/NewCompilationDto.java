package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.utility.Create;
import ru.practicum.utility.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
public class NewCompilationDto {

    private Boolean pinned;
    @NotBlank(groups = Create.class)
    @Size(max = 50, groups = {Create.class, Update.class})
    private String title;
    private List<Long> events;

}
