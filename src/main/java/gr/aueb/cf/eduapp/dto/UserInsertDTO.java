package gr.aueb.cf.eduapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserInsertDTO(
        @NotNull @Size(min = 3, max = 20)
    String username,
    @NotNull @Pattern(regexp = "(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*+=])^.{8,}$")
    String password,
    Long roleId
    )
{

}
