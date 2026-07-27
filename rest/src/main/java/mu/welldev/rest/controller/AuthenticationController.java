package mu.welldev.rest.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mu.welldev.service.KeycloakService;
import mu.welldev.service.dto.TokenResponse;
import mu.welldev.service.dto.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(version = "1.0", value = "/api/v1/auth", produces = "application/json")
public class AuthenticationController {
    private final KeycloakService keycloakService;

    @PostMapping
    public ResponseEntity<TokenResponse> register(@Validated @RequestBody UserRequest userRequest, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(keycloakService.register(userRequest, httpServletRequest));
    }
}
