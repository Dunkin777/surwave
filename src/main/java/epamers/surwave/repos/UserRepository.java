package epamers.surwave.repos;

import epamers.surwave.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  void deleteByUsername(String username);
}
