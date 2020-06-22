package epamers.surwave.unit.services;

import static epamers.surwave.TestUtils.OPTION_ID;
import static epamers.surwave.TestUtils.SURVEY_ID;
import static epamers.surwave.TestUtils.getValidClassicSurvey;
import static epamers.surwave.TestUtils.getValidOption;
import static epamers.surwave.core.ExceptionMessageContract.OPTION_NOT_FOUND;
import static epamers.surwave.core.ExceptionMessageContract.RESULTS_INVALID_SURVEY_STATE;
import static epamers.surwave.core.ExceptionMessageContract.SURVEY_IS_NULL_CREATION;
import static epamers.surwave.core.ExceptionMessageContract.SURVEY_IS_NULL_MODIFICATION;
import static epamers.surwave.core.ExceptionMessageContract.SURVEY_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import epamers.surwave.core.exceptions.ValidationException;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.SurveyType;
import epamers.surwave.entities.User;
import epamers.surwave.entities.Vote;
import epamers.surwave.repos.OptionRepository;
import epamers.surwave.repos.SurveyRepository;
import epamers.surwave.repos.VoteRepository;
import epamers.surwave.services.SongService;
import epamers.surwave.services.SurveyService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SurveyServiceTest {

  private static final Long OTHER_OPTION_ID = 2L;
  private static final Long NONEXISTENT_SURVEY_ID = 100L;
  private static final String CURRENT_USER_ID = "someGoogleId";

  @InjectMocks
  private SurveyService surveyService;

  @Mock
  private SurveyRepository surveyRepository;

  @Mock
  private OptionRepository optionRepository;

  @Mock
  private SongService songService;

  @Mock
  private VoteRepository voteRepository;

  private User currentUser;
  private Survey survey;
  private Option yourOption;
  private Option otherOption;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    currentUser = User.builder()
        .id(CURRENT_USER_ID)
        .build();

    yourOption = getValidOption();
    yourOption.setUser(currentUser);

    otherOption = getValidOption();
    otherOption.setId(OTHER_OPTION_ID);

    Set<Option> options = new HashSet<>();
    options.add(yourOption);
    options.add(otherOption);

    survey = getValidClassicSurvey();
    survey.setOptions(options);

    when(surveyRepository.existsById(SURVEY_ID)).thenReturn(true);
    when(surveyRepository.findById(SURVEY_ID)).thenReturn(Optional.of(survey));
    when(surveyRepository.findAll()).thenReturn(List.of(survey));
    when(surveyRepository.save(survey)).thenReturn(survey);
    when(optionRepository.findById(OPTION_ID)).thenReturn(Optional.of(yourOption));
    when(optionRepository.findById(OTHER_OPTION_ID)).thenReturn(Optional.of(otherOption));
  }

  @Test
  public void getAll_oneSurveyInRepo_gotOneSurvey() {
    List<Survey> surveys = surveyService.getAll();

    assertEquals(1, surveys.size());
    assertTrue(surveys.contains(survey));
  }

  @Test
  public void getById_existingId_success() {
    Survey foundSurvey = surveyService.getById(SURVEY_ID);

    assertEquals(survey, foundSurvey);
  }

  @Test
  public void getById_nonexistentID_exception() {
    String expectedMessage = String.format(SURVEY_NOT_FOUND, NONEXISTENT_SURVEY_ID);

    Throwable thrown = catchThrowable(() ->surveyService.getById(NONEXISTENT_SURVEY_ID));

    assertThat(thrown).isInstanceOf(EntityNotFoundException.class)
        .hasMessage(expectedMessage);
  }

  @Test
  public void getByIdWithSongURLs_existingId_success() {
    Survey foundSurvey = surveyService.getByIdWithSongURLs(SURVEY_ID);

    assertEquals(survey, foundSurvey);

    verify(songService).fillWithMediaUrl(any());
  }

  @Test
  public void getByIdWithSongURLs_nonexistentID_exception() {
    String expectedMessage = String.format(SURVEY_NOT_FOUND, NONEXISTENT_SURVEY_ID);

    Throwable thrown = catchThrowable(() -> surveyService.getByIdWithSongURLs(NONEXISTENT_SURVEY_ID));

    assertThat(thrown).isInstanceOf(EntityNotFoundException.class)
        .hasMessage(expectedMessage);
  }

  @Test
  public void create_validEntity_success() {
    Survey createdSurvey = surveyService.create(survey);

    verify(surveyRepository).save(survey);
    assertEquals(survey, createdSurvey);
    assertEquals(SurveyState.CREATED, survey.getState());
  }

  @Test
  public void create_nullSurvey_success() {
    Throwable thrown = catchThrowable(() -> surveyService.create(null));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
        .hasMessage(SURVEY_IS_NULL_CREATION);
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
    assertNotEquals(newProposalsByUser, survey.getProposalsByUser());
    assertNotEquals(SurveyType.RANGED, survey.getType());
  }

  @Test
  public void update_nonexistentId_exception() {
    String expectedMessage = String.format(SURVEY_NOT_FOUND, NONEXISTENT_SURVEY_ID);

    Throwable thrown = catchThrowable(() -> surveyService.update(NONEXISTENT_SURVEY_ID, survey));

    assertThat(thrown).isInstanceOf(EntityNotFoundException.class)
        .hasMessage(expectedMessage);
  }

  @Test
  public void update_nullSurvey_exception() {
    Throwable thrown = catchThrowable(() -> surveyService.update(SURVEY_ID, null));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
        .hasMessage(SURVEY_IS_NULL_MODIFICATION);
  }

  @Test
  public void removeOption_existentOption_songRemovedAndDeleted() {
    surveyService.removeOption(OPTION_ID);

    verify(optionRepository).delete(any());
  }

  @Test
  public void removeOption_nonExistentOption_nothingRemoved() {
    Long otherOptionId = 40L;
    String expectedMessage = String.format(OPTION_NOT_FOUND, otherOptionId);

    Throwable thrown = catchThrowable(() -> surveyService.removeOption(otherOptionId));

    verify(optionRepository, never()).delete(any());
    assertThat(thrown).isInstanceOf(EntityNotFoundException.class)
        .hasMessage(expectedMessage);
  }

  @Test
  public void getByIdFiltered_createdState_returnOnlyUserOptions() {
    Survey returnedSurvey = surveyService.getByIdFiltered(SURVEY_ID, currentUser);

    returnedSurvey.getOptions().forEach(o -> assertEquals(currentUser, o.getUser()));
  }

  @Test
  public void getByIdFiltered_startedState_returnNotUserOptions() {
    survey.setState(SurveyState.STARTED);

    Survey returnedSurvey = surveyService.getByIdFiltered(SURVEY_ID, currentUser);

    returnedSurvey.getOptions().forEach(o -> assertNotEquals(currentUser, o.getUser()));
  }

  @Test
  public void addVotes_allValid_voteSaved() {
    Vote vote = Vote.builder()
        .option(otherOption)
        .participant(currentUser)
        .rating(1)
        .build();

    List<Vote> votes = new ArrayList<>();
    votes.add(vote);

    surveyService.addVotes(votes);

    verify(voteRepository).save(any(Vote.class));
  }

  @Test
  public void getByIdForRating_allValid_returnSurvey() {
    survey.setState(SurveyState.STOPPED);

    Survey returnedSurvey = surveyService.getByIdForRating(SURVEY_ID);

    assertThat(returnedSurvey).isEqualTo(survey);
  }

  @Test
  public void getByIdForRating_wrongSurveyState_exception() {
    Throwable thrown = catchThrowable(() -> surveyService.getByIdForRating(SURVEY_ID));

    assertThat(thrown).isInstanceOf(ValidationException.class)
        .hasMessage(RESULTS_INVALID_SURVEY_STATE);
  }
}
