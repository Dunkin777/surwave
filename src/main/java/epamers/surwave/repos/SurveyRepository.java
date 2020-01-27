package epamers.surwave.repos;

import epamers.surwave.entities.Survey;
import epamers.surwave.repos.custom.CustomSurveyRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends CustomSurveyRepository, JpaRepository<Survey, Long> {

}
