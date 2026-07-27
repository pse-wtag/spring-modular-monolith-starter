package mu.welldev.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mu.welldev.persistence.entity.User;
import mu.welldev.persistence.repository.UserRepository;
import mu.welldev.service.KeycloakService;
import mu.welldev.service.KeycloakTokenProvider;
import mu.welldev.service.dto.TokenResponse;
import mu.welldev.service.dto.UserRequest;
import mu.welldev.service.exception.UsernameExistException;
import mu.welldev.service.mapper.user.UserMapper;
import mu.welldev.service.util.Credentials;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

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
        userRepository.findUserByUsername(userRequest.username())
                .ifPresent(user -> {
                    throw new UsernameExistException("Username: " + user.getUsername() + " already exist");
                });

        CredentialRepresentation credentialRepresentation = Credentials.createCredentialRepresentation(userRequest.password());
        UserRepresentation userRepresentation = userMapper.mapToUserRepresentation(userRequest, credentialRepresentation);

        try (Response response = usersResource.create(userRepresentation)) {
            if (response.getStatus() != 201) {
                String errorResponse = response.hasEntity() ? response.readEntity(String.class) : "No explicit details provided";
                log.error("Keycloak registration failed! Status: {}, Details: {}", response.getStatus(), errorResponse);

                throw new RuntimeException("Failed to create user in Keycloak. Status: " + response.getStatus() + " Details: " + errorResponse);
            }

            String keycloakId = response.getLocation().getPath()
                    .substring(response.getLocation().getPath().lastIndexOf("/") + 1);

            RoleRepresentation roleRepresentation = usersResource.get(keycloakId)
                    .roles()
                    .realmLevel()
                    .listAvailable()
                    .stream()
                    .filter(role -> role.getName().equals(userRequest.role().name()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Role not found: " + userRequest.role().name()));

            usersResource.get(keycloakId).roles().realmLevel().add(List.of(roleRepresentation));

            User user = userMapper.mapToUser(userRequest);
            user.setKeycloakId(keycloakId);

            userRepository.save(user);
            log.info("Username {} successfully created with keycloakId: {}", user.getUsername(), keycloakId);

            return authenticate(userRequest.username(), userRequest.password(), request);
        }
    }

    @Override
    public TokenResponse authenticate(String username, String password, HttpServletRequest request) {
        return keycloakTokenProvider.getToken(username, password);
    }
}
