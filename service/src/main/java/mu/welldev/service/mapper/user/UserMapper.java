package mu.welldev.service.mapper.user;

import mu.welldev.persistence.entity.User;
import mu.welldev.service.dto.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends KeycloakMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "firstName", target = "firstname")
    @Mapping(source = "lastName", target = "lastname")
    User mapToUser(UserRequest userRequest);
}
