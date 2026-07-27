package mu.welldev.rest.configuration;

import mu.welldev.service.KeycloakTokenProvider;
import mu.welldev.service.dto.TokenResponse;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfiguration implements KeycloakTokenProvider {

    private final String serverUrl;
    private final String realm;
    private final String clientId;
    private final String clientSecret;

    public KeycloakConfiguration(@Value("${application.keycloak.server-url}") String serverUrl,
                                 @Value("${application.keycloak.realm}") String realm,
                                 @Value("${application.keycloak.client-id}") String clientId,
                                 @Value("${application.keycloak.client-secret}") String clientSecret) {
        this.serverUrl = serverUrl;
        this.realm = realm;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Bean
    public Keycloak adminKeycloak() {
        return KeycloakBuilder
                .builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .resteasyClient(ResteasyClientBuilder.newBuilder().build())
                .build();
    }

    @Bean
    public RealmResource realmResource(Keycloak keycloak) {
        return keycloak.realm(realm);
    }

    @Bean
    public UsersResource usersResource(RealmResource realmResource) {
        return realmResource.users();
    }

    @Override
    public TokenResponse getToken(String username, String password) {
        try (var keycloakClient = KeycloakBuilder
                .builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .resteasyClient(ResteasyClientBuilder.newBuilder().build())
                .build()) {

            AccessTokenResponse tokenPayload = keycloakClient.tokenManager().getAccessToken();
            return TokenResponse.builder()
                    .accessToken(tokenPayload.getToken())
                    .refreshToken(tokenPayload.getRefreshToken())
                    .build();
        }

    }
}
