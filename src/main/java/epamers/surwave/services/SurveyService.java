package epamers.surwave.services;

import epamers.surwave.entities.Song;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.SurveyUserSongLink;
import epamers.surwave.entities.User;
import epamers.surwave.repos.SurveyRepository;
import epamers.surwave.repos.SurveyUserSongLinkRepository;
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
  private final SurveyUserSongLinkRepository surveyUserSongLinkRepository;

  public List<Survey> getAll() {
    return surveyRepository.findAll();
  }

  public Survey getById(Long id) {
    return surveyRepository.findById(id).orElseThrow();
  }

  public Survey getByIdForCurrentUser(Long id, User user) {
    Survey survey = surveyRepository.findById(id).orElseThrow();
    User currentUser = userService.getById(user.getId());

    Set<SurveyUserSongLink> susls = survey.getSurveyUserSongLinks().stream().filter(susl -> !susl.getUser().equals(currentUser))
        .collect(Collectors.toSet());

    survey.setSurveyUserSongLinks(susls);

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

    Set<SurveyUserSongLink> susl = getById(id).getSurveyUserSongLinks();

    survey.setId(id);
    survey.setSurveyUserSongLinks(susl);

    surveyRepository.save(survey);
  }

  @Transactional
  public Song addSong(Long surveyId, Song newSong, User user) {
    Survey survey = getById(surveyId);
    newSong = songService.getOrCreate(newSong);

    User currentUser = userService.getById(user.getId());
    Set<Song> proposedSongs = currentUser.getProposedSongs();
    proposedSongs.add(newSong);

    SurveyUserSongLink susl = new SurveyUserSongLink();
    susl.setUser(currentUser);
    susl.setSong(newSong);
    susl.setSurvey(survey);
    survey.addSong(susl);

    userService.save(currentUser);
    surveyRepository.save(survey);
    return newSong;
  }

  @Transactional
  public void removeSong(Long surveyId, Long songId) {
    Survey survey = getById(surveyId);
    Song song = songService.getById(songId);

    SurveyUserSongLink suslToRemove = survey.getSurveyUserSongLinks().stream().filter(susl -> susl.getSong().equals(song)).findFirst()
        .orElseThrow();

    if (survey.getSurveyUserSongLinks().remove(suslToRemove)){
      surveyRepository.save(survey);
      surveyUserSongLinkRepository.delete(suslToRemove);
    }
  }

  @Transactional
  public void updateState(Long id, SurveyState surveyState) {
    Survey survey = getById(id);
    survey.setState(surveyState);

    surveyRepository.save(survey);
  }
}
