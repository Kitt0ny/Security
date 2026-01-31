package org.example.Security.models.authDTO;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {

}
