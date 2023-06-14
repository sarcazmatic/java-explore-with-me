package ru.practicum.api.all.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicCompilationsServiceImpl implements PublicCompilationsService {

    private final CompilationRepository compilationRepository;

    public List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable) {
        return compilationRepository.findAllByPinned(pinned, pageable).stream().map(c -> CompilationMapper.toCompilationDto(c)).collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(long compId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("По данному id подборка не найдена!")
        ));
    }

}
