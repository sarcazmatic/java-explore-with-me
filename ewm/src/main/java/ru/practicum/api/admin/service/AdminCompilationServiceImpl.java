package ru.practicum.api.admin.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationMapper;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto postNewCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.fromNewCompilationDto(newCompilationDto);

        if (Optional.ofNullable(newCompilationDto.getEvents()).isPresent())
            compilation.setEvents(eventRepository.findAllById(newCompilationDto.getEvents()));

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public CompilationDto patchCompilation(long compId, NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Подборка не найдена!")
        );

        Optional.ofNullable(newCompilationDto.getEvents()).ifPresent(ge -> compilation.setEvents(eventRepository.findAllById(ge)));

        Optional.ofNullable(newCompilationDto.getTitle()).ifPresent(compilation::setTitle);

        Optional.ofNullable(newCompilationDto.getPinned()).ifPresent(compilation::setPinned);

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(long compId) {
        compilationRepository.deleteById(compId);
    }

}
