package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.event.EventShortDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
public class CompilationDto {

    @NotNull
    private long id;
    @NotNull
    private boolean pinned;
    @NotNull
    private String title;
    private List<EventShortDto> events;

}
