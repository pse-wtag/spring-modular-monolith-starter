package mu.welldev.persistence.repository;

import mu.welldev.persistence.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User> {
    Optional<User> findUserByUsername(String username);
}
