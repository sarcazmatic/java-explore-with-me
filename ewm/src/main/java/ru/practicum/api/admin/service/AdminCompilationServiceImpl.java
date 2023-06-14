package ru.practicum.api.admin.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationMapper;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationDto postNewCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.fromNewCompilationDto(newCompilationDto);

        if (Optional.ofNullable(newCompilationDto.getEvents()).isPresent())
            compilation.setEvents(eventRepository.findAllById(newCompilationDto.getEvents()));

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    public CompilationDto patchCompilation(long compId, NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Подборка не найдена!")
        );

        if (Optional.ofNullable(newCompilationDto.getEvents()).isPresent())
            compilation.setEvents(eventRepository.findAllById(newCompilationDto.getEvents()));

        if (Optional.ofNullable(newCompilationDto.getTitle()).isPresent())
            compilation.setTitle(newCompilationDto.getTitle());

        if ((Optional.ofNullable(newCompilationDto.getPinned()).isPresent()))
            compilation.setPinned(newCompilationDto.getPinned());

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    public void deleteCompilation(long compId) {
        compilationRepository.deleteById(compId);
    }

}
