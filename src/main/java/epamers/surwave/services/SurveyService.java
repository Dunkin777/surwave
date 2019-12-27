package epamers.surwave.services;

import static java.util.stream.Collectors.toSet;

import epamers.surwave.entities.Option;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.repos.SurveyRepository;
import java.util.List;
import java.util.NoSuchElementException;
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

    Set<Option> processedOptions = optionService.process(survey.getOptions());
    survey.setOptions(processedOptions);
    survey.setState(SurveyState.CREATED);
    return surveyRepository.save(survey);
  }

  @Transactional
  public void update(Long id, Survey survey) {

    if (!surveyRepository.existsById(id)) {
      throw new NoSuchElementException();
    }

    if (survey == null) {
      throw new IllegalArgumentException();
    }

    survey.setId(id);

    Set<Option> processedOptions = optionService.process(survey.getOptions());
    survey.setOptions(processedOptions);
    surveyRepository.save(survey);
  }

  @Transactional
  public void delete(Long id) {

    if (!surveyRepository.existsById(id)) {
      throw new NoSuchElementException();
    }
    surveyRepository.deleteById(id);
  }

  public void addOptions(Long id, List<Long> optionIds) {

    Survey survey = getById(id);
    Set<Option> options = optionIds.stream()
        .map(i -> Option.builder()
            .id(i)
            .build())
        .collect(toSet());

    Set<Option> optionsToSet = survey.getOptions();
    optionsToSet.addAll(optionService.process(options));
    survey.setOptions(optionsToSet);

    surveyRepository.save(survey);
  }

  public void updateState(Long id, SurveyState surveyState) {

    Survey survey = getById(id);
    survey.setState(surveyState);
    surveyRepository.save(survey);
  }
}
