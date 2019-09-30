package epamers.surwave.services;

import epamers.surwave.entities.Survey;
import epamers.surwave.repos.SurveyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SurveyService {

  private final SurveyRepository surveyRepository;

  public List<Survey> getAll() {

    return surveyRepository.findAll();
  }

  public Survey getById(Long id) {

    return surveyRepository.findById(id).orElseThrow();
  }

  @Transactional
  public Survey create(Survey survey) {

    if (survey == null) {
      throw new IllegalArgumentException();
    }
    return surveyRepository.save(survey);
  }

  @Transactional
  public void update(Long id, Survey survey) {

    if (survey == null) {
      throw new IllegalArgumentException();
    }

    surveyRepository.findById(id).orElseThrow();
    survey.setId(id);
    surveyRepository.save(survey);
  }

  @Transactional
  public void delete(Long id) {

    surveyRepository.findById(id).orElseThrow();
    surveyRepository.deleteById(id);
  }
}
