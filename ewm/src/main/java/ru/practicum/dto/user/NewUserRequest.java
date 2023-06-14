package ru.practicum.dto.user;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class NewUserRequest {

    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    private String email;
    @NotBlank
    @NotNull
    @Size(min = 2, max = 250)
    private String name;
}
