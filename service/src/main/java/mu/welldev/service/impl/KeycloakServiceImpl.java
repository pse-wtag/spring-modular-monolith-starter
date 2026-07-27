package mu.welldev.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mu.welldev.persistence.repository.UserRepository;
import mu.welldev.service.KeycloakService;
import mu.welldev.service.KeycloakTokenProvider;
import mu.welldev.service.dto.TokenResponse;
import mu.welldev.service.dto.UserRequest;
import mu.welldev.service.mapper.user.UserMapper;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final UsersResource usersResource;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakTokenProvider keycloakTokenProvider;

    @Override
    public TokenResponse register(UserRequest userRequest, HttpServletRequest request) {
        return null;
    }

    @Override
    public TokenResponse authenticate(String username, String password, HttpServletRequest request) {
        return null;
    }
}
