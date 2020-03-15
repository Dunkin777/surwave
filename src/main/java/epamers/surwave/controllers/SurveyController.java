package epamers.surwave.controllers;

import static epamers.surwave.core.Contract.OPTION_URL;
import static epamers.surwave.core.Contract.SURVEY_URL;
import static java.util.stream.Collectors.toList;

import epamers.surwave.dtos.OptionForm;
import epamers.surwave.dtos.SurveyForm;
import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.User;
import epamers.surwave.services.SurveyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping(SURVEY_URL)
public class SurveyController {

  private final SurveyService surveyService;
  private final ConversionService converter;

  @GetMapping("/all")
  @ApiOperation(
      value = "Get All Surveys",
      notes = "Returns all Surveys ever created."
  )
  public List<SurveyView> getAll() {
    return surveyService.getAll().stream()
        .map(s -> converter.convert(s, SurveyView.class))
        .collect(toList());
  }

  @GetMapping("/{id}")
  @ApiOperation(
      value = "Get Survey",
      notes = "Awaits Survey ID as path variable. Returns SurveyView. "
          + "Songs for all users, without filtration."
  )
  public SurveyView get(@ApiParam(value = "Survey ID") @PathVariable Long id) {
    Survey survey = surveyService.getById(id);

    return converter.convert(survey, SurveyView.class);
  }

  @GetMapping("/{id}/filtered")
  @ApiOperation(
      value = "Get Survey for current User",
      notes = "Awaits Survey ID as a path variable. Returns SurveyView. Requested Survey will "
          + "contain only Songs that can be used as voting options for the current user."
  )
  public SurveyView getForUser(@ApiIgnore @AuthenticationPrincipal User user,
      @ApiParam(value = "Survey ID") @PathVariable Long id) {
    Survey survey = surveyService.getByIdForCurrentUser(id, user);

    return converter.convert(survey, SurveyView.class);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(
      value = "Create Survey",
      notes = "Awaits SongForm as body. Returns new entity url in 'Location' header. "
          + "Creates new Survey with 0 Songs."
  )
  public void create(
      @ApiParam(value = "Data for new Survey") @RequestBody @Valid SurveyForm surveyForm,
      @ApiIgnore HttpServletResponse response) {
    Survey survey = converter.convert(surveyForm, Survey.class);
    Long surveyId = surveyService.create(survey).getId();
    response.addHeader("Location", SURVEY_URL + "/" + surveyId);
  }

  @PutMapping("/{id}")
  @ApiOperation(
      value = "Update Survey",
      notes = "Awaits Survey ID as a path variable and SurveyForm as body. Allows to change "
          + "basic Survey properties."
  )
  public void update(@ApiParam(value = "Survey ID") @PathVariable Long id,
      @ApiParam(value = "Updated Survey data") @RequestBody @Valid SurveyForm surveyForm) {
    Survey survey = converter.convert(surveyForm, Survey.class);
    surveyService.update(id, survey);
  }

  @PostMapping("/{surveyId}" + OPTION_URL)
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(
      value = "Create Option for Survey",
      notes = "Awaits OptionForm as body. Returns new entity url in 'Location' header. "
          + "Creates and adds to Survey new Option based on existing Song. Current user "
          + "will be remembered as Option creator."
  )
  public void addOption(@ApiIgnore @AuthenticationPrincipal User user, @ApiParam(value = "Survey ID") @PathVariable Long surveyId,
      @ApiParam(value = "Data for new Option") @RequestBody @Valid OptionForm optionForm, @ApiIgnore HttpServletResponse response) {
    Option option = converter.convert(optionForm, Option.class);
    Long optionId = surveyService.addOption(surveyId, option, user).getId();
    response.addHeader("Location", SURVEY_URL + "/" + optionId);
  }

  @DeleteMapping("/{surveyId}" + OPTION_URL + "/{optionId}")
  @ApiOperation(
      value = "Remove Option from Survey",
      notes = "Awaits Survey ID and Song ID as path variables. Allows to remove certain song from "
          + "specified survey."
  )
  public void removeOption(@ApiParam(value = "Survey ID") @PathVariable Long surveyId,
      @ApiParam(value = "Option ID") @PathVariable Long optionId) {
    surveyService.removeOption(surveyId, optionId);
  }
}
