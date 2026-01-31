package org.example.Security.models.authDTO;

public record RegisterRequest(
        String login,
        String password,
        String email
) {

}
