package ru.practicum.api.admin.service;


import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;

public interface AdminCompilationService {

    CompilationDto postNewCompilation(NewCompilationDto newCompilationDto);

    CompilationDto patchCompilation(long compId, NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);


}
