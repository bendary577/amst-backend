package accountmanager.supporttool.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record AppUserDTO(
        @NotBlank(message = "email is mandatory")
        @Size(max = 50)
        @Email
        String email,
        String name,
        boolean isBlocked,
        Set<RoleDTO> roles
) {}
