package epamers.surwave.unit.services;

import static epamers.surwave.TestUtils.OPTION_ID;
import static epamers.surwave.TestUtils.SONG_MEDIA_URL;
import static epamers.surwave.TestUtils.SONG_STORAGE_KEY;
import static epamers.surwave.TestUtils.SURVEY_ID;
import static epamers.surwave.TestUtils.getValidClassicSurvey;
import static epamers.surwave.TestUtils.getValidOption;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import epamers.surwave.core.exceptions.ResultsException;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Song;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.SurveyType;
import epamers.surwave.entities.User;
import epamers.surwave.entities.Vote;
import epamers.surwave.repos.OptionRepository;
import epamers.surwave.repos.SurveyRepository;
import epamers.surwave.repos.VoteRepository;
import epamers.surwave.services.MediaFileService;
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
  private MediaFileService mediaFileService;

  @Mock
  VoteRepository voteRepository;

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

    when(mediaFileService.getMediaPresignedUrl(SONG_STORAGE_KEY)).thenReturn(SONG_MEDIA_URL);
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

  @Test(expected = EntityNotFoundException.class)
  public void getById_nonexistentID_exception() {
    surveyService.getById(NONEXISTENT_SURVEY_ID);
  }

  @Test
  public void getByIdWithSongURLs_existingId_success() {
    Survey foundSurvey = surveyService.getByIdWithSongURLs(SURVEY_ID);

    assertEquals(survey, foundSurvey);

    assertThat(foundSurvey.getSongs())
        .extracting(Song::getMediaURL)
        .contains(SONG_MEDIA_URL);
  }

  @Test
  public void getByIdWithSongURLs_nonexistentID_exception() {
    assertThatThrownBy(() -> surveyService.getByIdWithSongURLs(NONEXISTENT_SURVEY_ID)).isInstanceOf(EntityNotFoundException.class);
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
    assertNotEquals(newProposalsByUser, survey.getProposalsByUser());
    assertNotEquals(SurveyType.RANGED, survey.getType());
  }

  @Test(expected = EntityNotFoundException.class)
  public void update_nonexistentId_exception() {
    surveyService.update(NONEXISTENT_SURVEY_ID, survey);
  }

  @Test(expected = IllegalArgumentException.class)
  public void update_nullSurvey_exception() {
    surveyService.update(SURVEY_ID, null);
  }

  @Test
  public void removeOption_existentOption_songRemovedAndDeleted() {
    surveyService.removeOption(OPTION_ID);

    verify(optionRepository).delete(any());
  }

  @Test(expected = EntityNotFoundException.class)
  public void removeOption_nonExistentOption_nothingRemoved() {
    Long otherOptionId = 40L;

    surveyService.removeOption(otherOptionId);

    verify(optionRepository, never()).delete(any());
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

  @Test(expected = ResultsException.class)
  public void getByIdForRating_wrongSurveyState_exception() {
    surveyService.getByIdForRating(SURVEY_ID);
  }
}
