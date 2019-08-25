package epamers.surwave.repos;

import epamers.surwave.entities.Option;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface OptionRepository extends CrudRepository<Option, Long> {

  List<Option> findByTitle(String title);

  List<Option> findByAuthor(String author);

}
