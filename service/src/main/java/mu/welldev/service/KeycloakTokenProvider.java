package mu.welldev.service;

import mu.welldev.service.dto.TokenResponse;

public interface KeycloakTokenProvider {
    TokenResponse getToken(String username, String password);
}
