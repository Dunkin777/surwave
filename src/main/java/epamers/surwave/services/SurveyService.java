package epamers.surwave.services;

import epamers.surwave.entities.Option;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.repos.SurveyRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SurveyService {

  private final SurveyRepository surveyRepository;
  private final OptionService optionService;

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

    processSurvey(survey);
    survey.setState(SurveyState.CREATED);
    return surveyRepository.save(survey);
  }

  @Transactional
  public void update(Long id, Survey survey) {

    if (survey == null) {
      throw new IllegalArgumentException();
    }



    if (!surveyRepository.existsById(id)) {
      throw new NoSuchElementException();
    }

    survey.setId(id);
    processSurvey(survey);
    surveyRepository.save(survey);
  }

  @Transactional
  public void delete(Long id) {

    if (!surveyRepository.existsById(id)) {
      throw new NoSuchElementException();
    }
    surveyRepository.deleteById(id);
  }

  public void processSurvey(Survey survey) {

    Set<Option> options = survey.getOptions().stream()
        .map(Option::getId)
        .map(optionService::getById)
        .collect(Collectors.toSet());

    survey.setOptions(options);
  }
}
