package epamers.surwave.services;

import epamers.surwave.entities.Option;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.repos.SurveyRepository;
import java.util.List;
import java.util.Set;
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

    survey.setState(SurveyState.CREATED);
    return surveyRepository.save(survey);
  }

  @Transactional
  public void update(Long id, Survey survey) {

    if (survey == null) {
      throw new IllegalArgumentException();
    }

    Set<Option> storedOptions = getById(id).getOptions();

    survey.setId(id);
    survey.setOptions(storedOptions);
    surveyRepository.save(survey);
  }

  @Transactional
  public void addOption(Long id, Option newOption) {

    Survey survey = getById(id);
    newOption = optionService.create(newOption);

    Set<Option> optionsToSet = survey.getOptions();
    optionsToSet.add(newOption);
    survey.setOptions(optionsToSet);

    surveyRepository.save(survey);
  }

  @Transactional
  public void updateState(Long id, SurveyState surveyState) {

    Survey survey = getById(id);
    survey.setState(surveyState);
    surveyRepository.save(survey);
  }
}
