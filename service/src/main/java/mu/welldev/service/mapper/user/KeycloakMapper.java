package mu.welldev.service.mapper.user;

import mu.welldev.service.dto.UserRequest;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;

@Mapper(componentModel = "spring", imports = {Collections.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KeycloakMapper {
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "credentials", expression = "java(Collections.singletonList(representation))")
    UserRepresentation mapToUserRepresentation(UserRequest request, CredentialRepresentation representation);
}
