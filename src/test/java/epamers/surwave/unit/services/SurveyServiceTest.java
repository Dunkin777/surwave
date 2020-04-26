package epamers.surwave.unit.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import epamers.surwave.core.exceptions.ResultsException;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Features;
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
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SurveyServiceTest {

  private static final Long SONG_ID = 156L;
  private static final Long OPTION_ID = 1L;
  private static final Long OTHER_OPTION_ID = 2L;
  private static final String SONG_PERFORMER = "Bee Gees";
  private static final String SONG_TITLE = "Komarinskaya (feat. Ella Fitzgerald)";
  private static final Long SURVEY_ID = 35L;
  private static final Long NONEXISTENT_SURVEY_ID = 100L;
  private static final String SURVEY_DESCRIPTION = "Please think twice before choosing!";
  private static final String CURRENT_USER_ID = "someGoogleId";
  private static final String OTHER_USER_ID = "anotherGoogleId";
  private static final Integer SURVEY_PROPOSALS_BY_USER = 5;

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
  private User otherUser;
  private Survey survey;
  private Option yourOption;
  private Option otherOption;
  private Vote vote;
  private Set<Option> options;
  private List<Vote> votes;
  private Song song;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    Features features = Features.builder()
        .danceability(1.0)
        .energy(0.1)
        .valence(0.5)
        .build();

    song = Song.builder()
        .performer(SONG_PERFORMER)
        .title(SONG_TITLE)
        .storageKey("")
        .id(SONG_ID)
        .features(features)
        .build();

    currentUser = User.builder()
        .id(CURRENT_USER_ID)
        .build();

    otherUser = User.builder()
        .id(OTHER_USER_ID)
        .build();

    yourOption = Option.builder()
        .id(OPTION_ID)
        .user(currentUser)
        .song(song)
        .survey(survey)
        .votes(new HashSet<>())
        .build();

    otherOption = Option.builder()
        .id(OTHER_OPTION_ID)
        .user(otherUser)
        .song(song)
        .survey(survey)
        .votes(new HashSet<>())
        .build();

    options = new HashSet<>();
    options.add(yourOption);
    options.add(otherOption);

    currentUser.setOptions(options);

    survey = ClassicSurvey.builder()
        .state(SurveyState.CREATED)
        .choicesByUser(1)
        .description(SURVEY_DESCRIPTION)
        .id(SURVEY_ID)
        .proposalsByUser(SURVEY_PROPOSALS_BY_USER)
        .options(options)
        .build();

    vote = Vote.builder()
        .option(otherOption)
        .participant(currentUser)
        .rating(1)
        .build();

    votes = new ArrayList<>();
    votes.add(vote);

    when(surveyRepository.findById(SURVEY_ID)).thenReturn(Optional.of(survey));
    when(surveyRepository.findAll()).thenReturn(List.of(survey));
    when(surveyRepository.save(survey)).thenReturn(survey);
    when(surveyRepository.existsById(SURVEY_ID)).thenReturn(true);

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

  @Test(expected = EntityNotFoundException.class)
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
    when(optionRepository.findById(OPTION_ID)).thenReturn(Optional.of(yourOption));

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
    Option otherOption = Option.builder()
        .song(Song.builder().build())
        .user(User.builder()
            .id("anotherUserId")
            .build())
        .build();
    options.add(otherOption);

    Survey returnedSurvey = surveyService.getByIdFiltered(SURVEY_ID, currentUser);

    returnedSurvey.getOptions().forEach(o -> assertEquals(currentUser, o.getUser()));
  }

  @Test
  public void getByIdFiltered_startedState_returnNotUserOptions() {
    Option otherOption = Option.builder()
        .song(Song.builder().build())
        .user(User.builder()
            .id("anotherUserId")
            .build())
        .build();
    options.add(otherOption);
    survey.setState(SurveyState.STARTED);

    Survey returnedSurvey = surveyService.getByIdFiltered(SURVEY_ID, currentUser);

    returnedSurvey.getOptions().forEach(o -> assertNotEquals(currentUser, o.getUser()));
  }

  @Test
  public void addVotes_allValid_voteSaved() {
    surveyService.addVotes(votes);

    verify(voteRepository).save(any(Vote.class));
  }

  @Test
  public void getByIdForRating_allValid_returnSurvey() {
    survey.setState(SurveyState.STOPPED);

    Survey returnedSurvey = surveyService.getByIdForRating(SURVEY_ID);

    Assertions.assertThat(returnedSurvey).isEqualTo(survey);
  }

  @Test(expected = ResultsException.class)
  public void getByIdForRating_wrongSurveyState_exception() {
    surveyService.getByIdForRating(SURVEY_ID);
  }

  @Test(expected = ResultsException.class)
  public void getByIdForRating_noFeatures_exception() {
    survey.setState(SurveyState.STOPPED);
    song.setFeatures(null);

    surveyService.getByIdForRating(SURVEY_ID);
  }
}
