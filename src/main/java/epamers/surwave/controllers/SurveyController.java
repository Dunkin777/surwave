package epamers.surwave.controllers;

import static epamers.surwave.core.Contract.OPTION_URL;
import static epamers.surwave.core.Contract.SURVEY_URL;

import epamers.surwave.dtos.OptionForm;
import epamers.surwave.dtos.OptionView;
import epamers.surwave.entities.Survey;
import epamers.surwave.services.SurveyService;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(SURVEY_URL)
public class SurveyController {

  private final SurveyService surveyService;
  private final ConversionService converter;

  @GetMapping("/all")
  public List<OptionView> getAllSurveys() {

    return surveyService.getAll().stream()
        .map(o -> converter.convert(o, OptionView.class))
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public OptionView getSurvey(@PathVariable Long id) {

    Survey survey = surveyService.getById(id);
    return converter.convert(survey, OptionView.class);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createSurvey(@RequestBody @Valid OptionForm optionForm, HttpServletResponse response) {

    Survey survey = surveyService.create(converter.convert(optionForm, Survey.class));
    response.addHeader("Location", OPTION_URL + "/" + survey.getId());
  }

  @PutMapping("/{id}")
  public void updateSurvey(@PathVariable Long id, @RequestBody @Valid OptionForm optionForm) {

    surveyService.update(id, converter.convert(optionForm, Survey.class));
  }

  @DeleteMapping("/{id}")
  public void deleteSurvey(@PathVariable Long id) {

    surveyService.delete(id);
  }
}
