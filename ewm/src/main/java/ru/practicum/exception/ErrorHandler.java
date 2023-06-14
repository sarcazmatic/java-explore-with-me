package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.utility.EWMDateTimePattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice("ru.practicum.api")
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final MethodArgumentNotValidException e) {
        log.info("400 {}", e.getMessage(), e);
        return ApiError.builder()
                .errors(Arrays.asList(e.getSuppressedFields()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(EWMDateTimePattern.FORMATTER)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.info("404 {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(EWMDateTimePattern.FORMATTER)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleCustomValidationException(final ValidationException e) {
        log.info("400 {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(EWMDateTimePattern.FORMATTER)))
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleForbiddenException(final DataIntegrityViolationException e) {
        log.info("409 {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(EWMDateTimePattern.FORMATTER)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleBadInputException(final ForbiddenException e) {
        log.info("409 {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(EWMDateTimePattern.FORMATTER)))
                .build();
    }
}
