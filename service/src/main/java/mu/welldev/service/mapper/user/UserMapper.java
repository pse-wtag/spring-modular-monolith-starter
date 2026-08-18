package mu.welldev.service.mapper.user;

import mu.welldev.persistence.entity.User;
import mu.welldev.service.dto.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;

@Mapper(componentModel = "spring", imports = {Collections.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends KeycloakMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userRequest.firstName", target = "firstname")
    @Mapping(source = "userRequest.lastName", target = "lastname")
    @Mapping(source = "keycloakId", target = "keycloakId")
    @Mapping(target = "role", constant = "USER")
    User mapToUser(UserRequest userRequest, String keycloakId);
}
