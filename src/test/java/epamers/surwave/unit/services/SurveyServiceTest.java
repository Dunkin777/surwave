package epamers.surwave.unit.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Song;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.User;
import epamers.surwave.repos.SurveyRepository;
import epamers.surwave.repos.OptionRepository;
import epamers.surwave.services.SongService;
import epamers.surwave.services.SurveyService;
import epamers.surwave.services.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SurveyServiceTest {

  @InjectMocks
  SurveyService surveyService;

  @Mock
  SongService songService;

  @Mock
  UserService userService;

  @Mock
  SurveyRepository surveyRepository;

  @Mock
  OptionRepository optionRepository;

  @Mock
  User user;

  private final Long SONG_ID = 156L;
  private final String PERFORMER = "Bee Gees";
  private final String TITLE = "Komarinskaya (feat. Ella Fitzgerald)";
  private final String COMMENT = "Starts in D#, then sudden change to another religion.";

  private final Long SURVEY_ID = 35L;
  private final Long NONEXISTENT_SURVEY_ID = 100L;
  private final String SURVEY_DESCRIPTION = "Please think twice before choosing!";
  private Song song;

  private String USER_ID = "someGoogleId";

  private Survey survey;

  private Option option;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    song = Song.builder()
        .performer(PERFORMER)
        .title(TITLE)
        .id(SONG_ID)
        .comment(COMMENT)
        .build();

    option = new Option();
    option.setSong(song);
    option.setUser(user);
    option.setSurvey(survey);

    Set<Option> options = new HashSet<>();
    options.add(option);

    survey = ClassicSurvey.builder()
        .choicesByUser(3)
        .description(SURVEY_DESCRIPTION)
        .id(SURVEY_ID)
        .proposalsByUser(3)
        .options(options)
        .build();

    when(surveyRepository.findById(SURVEY_ID)).thenReturn(Optional.of(survey));
    when(surveyRepository.findAll()).thenReturn(List.of(survey));
    when(surveyRepository.save(survey)).thenReturn(survey);
    when(surveyRepository.existsById(SURVEY_ID)).thenReturn(true);
    when(user.getId()).thenReturn(USER_ID);
    when(user.getProposedSongs()).thenReturn(new HashSet<>());
  }

  @Test
  public void getAll_success() {
    List<Survey> surveys = surveyService.getAll();

    assertEquals(1, surveys.size());
    assertTrue(surveys.contains(survey));
  }

  @Test
  public void getById_existingId_success() {
    Survey foundSurvey = surveyService.getById(SURVEY_ID);

    assertEquals(survey, foundSurvey);
  }

  @Test(expected = NoSuchElementException.class)
  public void getById_nonexistentID_exception() {
    surveyService.getById(NONEXISTENT_SURVEY_ID);
  }

  @Test
  public void create_validEntity_success() {
    Survey createdSurvey = surveyService.create(survey);

    verify(surveyRepository).save(survey);
    assertEquals(survey, createdSurvey);
    assertEquals(SurveyState.CREATED, survey.getState());
  }

  @Test(expected = IllegalArgumentException.class)
  public void create_nullSurvey_success() {
    surveyService.create(null);
  }

  @Test
  public void update_validArguments_success() {
    surveyService.update(SURVEY_ID, survey);

    verify(surveyRepository).save(survey);
  }

  @Test(expected = NoSuchElementException.class)
  public void update_nonexistentId_exception() {
    surveyService.update(NONEXISTENT_SURVEY_ID, survey);
  }

  @Test(expected = IllegalArgumentException.class)
  public void update_nullSurvey_exception() {
    surveyService.update(SURVEY_ID, null);
  }

  @Test
  public void addSong_validArguments_success() {
    when(songService.getOrCreate(song)).thenReturn(song);
    when(userService.getById(USER_ID)).thenReturn(user);
    ArgumentCaptor<Survey> arg = ArgumentCaptor.forClass(Survey.class);
    survey.setOptions(new HashSet<>());

    surveyService.addSong(SURVEY_ID, song, user);

    verify(surveyRepository).save(arg.capture());
    assertEquals(survey, arg.getValue());
    assertTrue(arg.getValue().getSongs().contains(song));
  }

  @Test
  public void removeSong_existentSong_songRemovedAndDeleted() {
    when(songService.getById(SONG_ID)).thenReturn(song);
    ArgumentCaptor<Survey> capturedSurvey = ArgumentCaptor.forClass(Survey.class);
    ArgumentCaptor<Option> capturedOption = ArgumentCaptor.forClass(Option.class);

    surveyService.removeSong(SURVEY_ID, SONG_ID);

    verify(surveyRepository).save(capturedSurvey.capture());
    verify(optionRepository).delete(capturedOption.capture());
    assertTrue(capturedSurvey.getValue().getSongs().isEmpty());
  }

  @Test(expected = NoSuchElementException.class)
  public void removeSong_nonExistentSong_nothingRemoved() {
    Long otherSongId = 40L;
    Song otherSong = Song.builder()
        .id(otherSongId)
        .build();
    when(songService.getById(otherSongId)).thenReturn(otherSong);

    surveyService.removeSong(SURVEY_ID, otherSongId);

    verify(surveyRepository, never()).save(any());
    verify(songService, never()).delete(any());
    verify(optionRepository, never()).delete(any());
  }

  @Test
  public void updateState_validArguments_success() {
    ArgumentCaptor<Survey> arg = ArgumentCaptor.forClass(Survey.class);

    surveyService.updateState(SURVEY_ID, SurveyState.STOPPED);

    verify(surveyRepository).save(arg.capture());
    assertEquals(survey, arg.getValue());
    assertEquals(SurveyState.STOPPED, arg.getValue().getState());
  }
}