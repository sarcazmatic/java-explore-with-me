package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;

@Builder
@Data
public class UpdateCompilationRequest {

    private boolean pinned;
    private String title;
    private List<EventShortDto> events;

}
