package epamers.surwave.services;

import epamers.surwave.entities.Song;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.User;
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
  private final SongService songService;
  private final UserService userService;

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

    Set<Song> storedSongs = getById(id).getSongs();

    survey.setId(id);
    survey.setSongs(storedSongs);

    surveyRepository.save(survey);
  }

  @Transactional
  public Song addSong(Long surveyId, Song newSong, User user) {
    Survey survey = getById(surveyId);
    newSong = songService.create(newSong);

    User currentUser = userService.getById(user.getId());
    Set<Song> proposedSongs = currentUser.getProposedSongs();
    proposedSongs.add(newSong);

    Set<Song> songsToUpdate = survey.getSongs();
    songsToUpdate.add(newSong);

    userService.update(currentUser);
    surveyRepository.save(survey);
    return newSong;
  }

  @Transactional
  public void removeSong(Long surveyId, Long songId) {
    Survey survey = getById(surveyId);
    Song song = songService.getById(songId);

    Set<Song> songs = survey.getSongs();

    if (songs.remove(song)) {
      surveyRepository.save(survey);
    }
  }

  @Transactional
  public void updateState(Long id, SurveyState surveyState) {
    Survey survey = getById(id);
    survey.setState(surveyState);

    surveyRepository.save(survey);
  }
}
