package epamers.surwave.unit.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Song;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.SurveyType;
import epamers.surwave.entities.User;
import epamers.surwave.repos.OptionRepository;
import epamers.surwave.repos.SurveyRepository;
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
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SurveyServiceTest {

  private static final Long SONG_ID = 156L;
  private static final Long OPTION_ID = 15L;
  private static final String SONG_PERFORMER = "Bee Gees";
  private static final String SONG_TITLE = "Komarinskaya (feat. Ella Fitzgerald)";
  private static final Long SURVEY_ID = 35L;
  private static final Long NONEXISTENT_SURVEY_ID = 100L;
  private static final String SURVEY_DESCRIPTION = "Please think twice before choosing!";
  private static final String USER_ID = "someGoogleId";
  private static final Integer SURVEY_PROPOSALS_BY_USER = 5;

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

  private Survey survey;
  private Song song;
  private Option option;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    song = Song.builder()
        .performer(SONG_PERFORMER)
        .title(SONG_TITLE)
        .id(SONG_ID)
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
        .proposalsByUser(SURVEY_PROPOSALS_BY_USER)
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
  public void update_validArguments_onlyAllowedFieldsUpdated() {
    final String newDescription = "Changed description";
    final String newTitle = "Changed title";
    final Integer newProposalsByUser = 66;

    Survey surveyNewValues = ClassicSurvey.builder()
        .title(newTitle)
        .description(newDescription)
        .proposalsByUser(newProposalsByUser)
        .type(SurveyType.RANGED)
        .build();

    surveyService.update(SURVEY_ID, surveyNewValues);

    assertEquals(newDescription, survey.getDescription());
    assertEquals(newTitle, survey.getTitle());
    assertEquals(SURVEY_PROPOSALS_BY_USER, survey.getProposalsByUser());
    assertEquals(SurveyType.CLASSIC, survey.getType());
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
  public void removeOption_existentOption_songRemovedAndDeleted() {
    when(optionRepository.findById(OPTION_ID)).thenReturn(Optional.of(option));

    surveyService.removeOption(OPTION_ID);

    verify(optionRepository).delete(any());
  }

  @Test(expected = NoSuchElementException.class)
  public void removeOption_nonExistentOption_nothingRemoved() {
    Long otherOptionId = 40L;

    surveyService.removeOption(otherOptionId);

    verify(optionRepository, never()).delete(any());
  }
}
