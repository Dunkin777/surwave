package epamers.surwave.repos;

import epamers.surwave.entities.Song;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

  Optional<Song> findByTitleIgnoreCaseAndPerformerIgnoreCase(String title, String performer);
}
