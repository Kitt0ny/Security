package org.example.Security.models.authDTO;

public record AuthRequest(
        String login,
        String password
) {

}
