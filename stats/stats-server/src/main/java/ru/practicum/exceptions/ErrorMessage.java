package ru.practicum.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {

    private String message;
    private HttpStatus status;
    private String timestamp;

}
