package org.example.Security.models;

import java.time.LocalDateTime;

public record PersonDTO(
        String login,
        String email,
        String role,
        LocalDateTime registrationDate
) {

}
