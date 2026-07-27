package mu.welldev.service;

import jakarta.servlet.http.HttpServletRequest;
import mu.welldev.service.dto.TokenResponse;
import mu.welldev.service.dto.UserRequest;

public interface KeycloakService {

    TokenResponse register(UserRequest userRequest, HttpServletRequest request);

    TokenResponse authenticate(String username, String password, HttpServletRequest request);
}
