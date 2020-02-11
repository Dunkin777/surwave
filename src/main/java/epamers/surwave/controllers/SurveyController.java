package epamers.surwave.controllers;

import static epamers.surwave.core.Contract.SONG_URL;
import static epamers.surwave.core.Contract.SURVEY_URL;

import epamers.surwave.dtos.SongForm;
import epamers.surwave.dtos.SurveyForm;
import epamers.surwave.dtos.SurveyView;
import epamers.surwave.entities.Song;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.User;
import epamers.surwave.services.SurveyService;
import java.util.List;
import java.util.stream.Collectors;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(SURVEY_URL)
public class SurveyController {

  private final SurveyService surveyService;
  private final ConversionService converter;

  @GetMapping("/all")
  public List<SurveyView> getAllSurveys() {
    return surveyService.getAll().stream()
        .map(s -> converter.convert(s, SurveyView.class))
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public SurveyView getSurvey(@PathVariable Long id) {
    Survey survey = surveyService.getById(id);
    return converter.convert(survey, SurveyView.class);
  }

  @GetMapping("/{id}/filtered")
  public SurveyView getSurveyForUser(@AuthenticationPrincipal User user, @PathVariable Long id) {
    Survey survey = surveyService.getByIdForCurrentUser(id, user);
    return converter.convert(survey, SurveyView.class);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createSurvey(@RequestBody @Valid SurveyForm surveyForm, HttpServletResponse response) {
    Survey survey = surveyService.create(converter.convert(surveyForm, Survey.class));
    response.addHeader("Location", SURVEY_URL + "/" + survey.getId());
  }

  @PutMapping("/{id}")
  public void updateSurvey(@PathVariable Long id, @RequestBody @Valid SurveyForm surveyForm) {
    surveyService.update(id, converter.convert(surveyForm, Survey.class));
  }

  @PutMapping("/{id}/song")
  public void addSongToSurvey(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody @Valid SongForm songForm,
      HttpServletResponse response) {
    Song song = converter.convert(songForm, Song.class);
    Song createdSong = surveyService.addSong(id, song, user);
    response.addHeader("Location", SONG_URL + "/" + createdSong.getId());
  }

  @DeleteMapping("/{surveyId}" + SONG_URL + "/{songId}")
  public void removeSongFromSurvey(@PathVariable Long surveyId, @PathVariable Long songId) {
    surveyService.removeSong(surveyId, songId);
  }
}
