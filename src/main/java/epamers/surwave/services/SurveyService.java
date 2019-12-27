package epamers.surwave.services;

import epamers.surwave.entities.Song;
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
  private final SongService songService;

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
  public void addOption(Long id, Song newSong) {
    Survey survey = getById(id);
    newSong = songService.create(newSong);

    Set<Song> optionsToSet = survey.getSongs();
    optionsToSet.add(newSong);
    survey.setSongs(optionsToSet);

    surveyRepository.save(survey);
  }

  @Transactional
  public void updateState(Long id, SurveyState surveyState) {
    Survey survey = getById(id);
    survey.setState(surveyState);

    surveyRepository.save(survey);
  }
}
