package epamers.surwave.repos;

import epamers.surwave.entities.ClassicSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassicSurveyRepository extends JpaRepository<ClassicSurvey, Long> {

}
