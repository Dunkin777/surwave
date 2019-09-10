package epamers.surwave.repos;

import epamers.surwave.entities.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

  List<Option> findByTitle(String title);

  List<Option> findByAuthor(String author);
}
