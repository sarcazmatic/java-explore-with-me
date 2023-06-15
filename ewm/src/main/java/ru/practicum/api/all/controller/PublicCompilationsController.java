package ru.practicum.api.all.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.all.service.PublicCompilationsService;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.utility.PageableMaker;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationsController {

    private final PublicCompilationsService publicCompilationsService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam (required = false) Boolean pinned,
                                                @RequestParam (required = false, defaultValue = "0") int from,
                                                @RequestParam (required = false, defaultValue = "10") int size) {
        return publicCompilationsService.getCompilations(pinned, PageableMaker.makePageable(from, size));
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        return publicCompilationsService.getCompilationById(compId);
    }

}
