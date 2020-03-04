package epamers.surwave.controllers;

import static epamers.surwave.core.Contract.SONG_URL;
import static epamers.surwave.core.Contract.SURVEY_URL;
import static java.util.stream.Collectors.toList;

import epamers.surwave.dtos.SurveyForm;
import epamers.surwave.dtos.SurveyView;
import epamers.surwave.dtos.VoteForm;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.User;
import epamers.surwave.entities.Vote;
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
  public List<SurveyView> getAllSurveys() {
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
  public SurveyView getSurvey(@ApiParam(value = "Survey ID") @PathVariable Long id) {
    Survey survey = surveyService.getById(id);

    return converter.convert(survey, SurveyView.class);
  }

  @GetMapping("/{id}/filtered")
  @ApiOperation(
      value = "Get Survey for current User",
      notes = "Awaits Survey ID as a path variable. Returns SurveyView. Requested Survey will "
          + "contain only Songs that can be used as voting options for the current user."
  )
  public SurveyView getSurveyForUser(@ApiIgnore @AuthenticationPrincipal User user,
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
  public void createSurvey(
      @ApiParam(value = "Data for new Survey") @RequestBody @Valid SurveyForm surveyForm,
      @ApiIgnore HttpServletResponse response) {
    Survey survey = surveyService.create(converter.convert(surveyForm, Survey.class));
    response.addHeader("Location", SURVEY_URL + "/" + survey.getId());
  }

  @PutMapping("/{id}")
  @ApiOperation(
      value = "Update Survey",
      notes = "Awaits Survey ID as a path variable and SurveyForm as body. Allows to change "
          + "basic Survey properties."
  )
  public void updateSurvey(@ApiParam(value = "Survey ID") @PathVariable Long id,
      @ApiParam(value = "Updated Survey data") @RequestBody @Valid SurveyForm surveyForm) {
    surveyService.update(id, converter.convert(surveyForm, Survey.class));
  }

  @DeleteMapping("/{surveyId}" + SONG_URL + "/{songId}")
  @ApiOperation(
      value = "Remove Song from Survey",
      notes = "Awaits Survey ID and Song ID as path variables. Allows to remove certain song from "
          + "specified survey."
  )
  public void removeSongFromSurvey(@ApiParam(value = "Survey ID") @PathVariable Long surveyId,
      @ApiParam(value = "Song ID") @PathVariable Long songId) {
    surveyService.removeSong(surveyId, songId);
  }

  @PutMapping("/{surveyId}/vote")
  @ApiOperation(
      value = "Add Collection of Votes to Survey",
      notes = "Awaits Survey ID as a path variable and Collection of VoteForms as body."
  )
  public void addVotes(@ApiIgnore @AuthenticationPrincipal User user, @ApiParam(value = "Survey ID") @PathVariable Long surveyId,
      @ApiParam(value = "Votes to add") @RequestBody @Valid List<VoteForm> voteForms) {
    List<Vote> votes = voteForms.stream()
        .map(voteForm -> converter.convert(voteForm, Vote.class))
        .collect(toList());

    surveyService.addVotes(surveyId, votes);
  }
}
