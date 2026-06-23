package gr.aueb.cf.eduapp.dto;

import java.util.UUID;

public record UserReadOnlyDTO(

        UUID uuid,
        String username,
        String roleId
) {
}
