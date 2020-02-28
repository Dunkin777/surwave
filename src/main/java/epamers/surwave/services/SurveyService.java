package epamers.surwave.services;

import epamers.surwave.entities.Song;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.User;
import epamers.surwave.repos.SurveyRepository;
import epamers.surwave.repos.OptionRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SurveyService {

  private final SurveyRepository surveyRepository;
  private final SongService songService;
  private final UserService userService;
  private final OptionRepository optionRepository;

  public List<Survey> getAll() {
    return surveyRepository.findAll();
  }

  public Survey getById(Long id) {
    return surveyRepository.findById(id).orElseThrow();
  }

  public Survey getByIdForCurrentUser(Long id, User user) {
    Survey survey = surveyRepository.findById(id).orElseThrow();
    User currentUser = userService.getById(user.getId());

    Set<Option> options = survey.getOptions().stream()
        .filter(option -> !option.getUser().equals(currentUser))
        .collect(Collectors.toSet());

    survey.setOptions(options);

    return survey;
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

    Set<Option> option = getById(id).getOptions();

    survey.setId(id);
    survey.setOptions(option);

    surveyRepository.save(survey);
  }

  @Transactional
  public Song addSong(Long surveyId, Song newSong, User user) {
    Survey survey = getById(surveyId);
    newSong = songService.getOrCreate(newSong);

    User currentUser = userService.getById(user.getId());
    Set<Song> proposedSongs = currentUser.getProposedSongs();
    proposedSongs.add(newSong);

    Option option = new Option();
    option.setUser(currentUser);
    option.setSong(newSong);
    option.setSurvey(survey);
    survey.addSong(option);

    userService.save(currentUser);
    surveyRepository.save(survey);
    return newSong;
  }

  @Transactional
  public void addOptions(Long surveyId, List<Option> options, User user) {
    Survey survey = getById(surveyId);
    for(Option option : options) {
      Song song = songService.getById(option.getSong().getId());
    }

  }

  @Transactional
  public void removeSong(Long surveyId, Long songId) {
    Survey survey = getById(surveyId);
    Song song = songService.getById(songId);

    Option optionToRemove = survey.getOptions().stream()
        .filter(option -> option.getSong().equals(song))
        .findFirst()
        .orElseThrow();

    if (survey.getOptions().remove(optionToRemove)) {
      surveyRepository.save(survey);
      optionRepository.delete(optionToRemove);
    }
  }

  @Transactional
  public void updateState(Long id, SurveyState surveyState) {
    Survey survey = getById(id);
    survey.setState(surveyState);

    surveyRepository.save(survey);
  }
}
