package ru.practicum.api.all.service;


import org.springframework.data.domain.Pageable;
import ru.practicum.dto.compilation.CompilationDto;

import java.util.List;

public interface PublicCompilationsService {

    List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable);

    CompilationDto getCompilationById(long compId);

}
