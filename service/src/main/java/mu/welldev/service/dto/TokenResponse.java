package mu.welldev.service.dto;

import lombok.Builder;

import java.util.Objects;

@Builder
public record TokenResponse(String accessToken, String refreshToken) {
    public TokenResponse {
        Objects.requireNonNull(accessToken);
        Objects.requireNonNull(refreshToken);
    }
}
