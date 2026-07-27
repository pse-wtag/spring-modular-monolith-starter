package mu.welldev.persistence.repository;

import mu.welldev.persistence.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User> {
}
