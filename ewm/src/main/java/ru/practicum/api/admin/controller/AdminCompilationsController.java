package ru.practicum.api.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.admin.service.AdminCompilationService;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.utility.Create;
import ru.practicum.utility.Update;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
@Slf4j
public class AdminCompilationsController {

    private final AdminCompilationService adminCompilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postNewCompilation(HttpServletRequest httpServletRequest,
                                             @RequestBody @Validated({Create.class}) NewCompilationDto newCompilationDto) {
        return adminCompilationService.postNewCompilation(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patchCompilation(HttpServletRequest httpServletRequest,
                                             @PathVariable long compId,
                                             @RequestBody @Validated({Update.class}) NewCompilationDto newCompilationDto) {
        return adminCompilationService.patchCompilation(compId, newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void postNewCompilation(HttpServletRequest httpServletRequest,
                                             @PathVariable long compId) {
        adminCompilationService.deleteCompilation(compId);
    }


}
