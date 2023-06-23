package ru.practicum.api.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.admin.service.AdminCompilationService;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.utility.Create;
import ru.practicum.utility.Update;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {

    private final AdminCompilationService adminCompilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postNewCompilation(@RequestBody
                                                 @Validated({Create.class}) NewCompilationDto newCompilationDto) {
        return adminCompilationService.postNewCompilation(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patchCompilation(@PathVariable long compId,
                                           @RequestBody @Validated({Update.class}) NewCompilationDto newCompilationDto) {
        return adminCompilationService.patchCompilation(compId, newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void postNewCompilation(@PathVariable long compId) {
        adminCompilationService.deleteCompilation(compId);
    }

}
