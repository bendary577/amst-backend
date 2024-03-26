package accountmanager.supporttool.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AppUserDTO(
        @NotBlank(message = "email is mandatory")
        @Size(max = 50)
        @Email
        String email,
        boolean isBlocked
) {}
