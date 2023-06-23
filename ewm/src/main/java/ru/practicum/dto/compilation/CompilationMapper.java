package ru.practicum.dto.compilation;

import ru.practicum.dto.event.EventMapper;
import ru.practicum.model.Compilation;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = CompilationDto.builder()
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .id(compilation.getId())
                .build();

        Optional.ofNullable(compilation.getEvents()).ifPresent(
                list -> compilationDto.setEvents(list.stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList())));

        return compilationDto;
    }

    public static Compilation fromNewCompilationDto(@NotNull NewCompilationDto newCompilationDto) {
        Compilation compilation = Compilation.builder()
                .title(newCompilationDto.getTitle())
                .build();

        Optional.ofNullable(newCompilationDto.getPinned()).ifPresent(compilation::setPinned);

        return compilation;
    }

}
