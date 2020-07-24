package epamers.surwave.validators;

import static epamers.surwave.core.ExceptionMessageContract.VOTING_ALREADY_VOTED;
import static epamers.surwave.core.ExceptionMessageContract.VOTING_CHOICES_BY_USER_NOT_SATISFIED;
import static epamers.surwave.core.ExceptionMessageContract.VOTING_FOR_YOUR_OPTION;
import static epamers.surwave.core.ExceptionMessageContract.VOTING_FOR_ZERO_OPTIONS;
import static epamers.surwave.core.ExceptionMessageContract.VOTING_INVALID_RATING_FOR_CLASSIC_TYPE;
import static epamers.surwave.core.ExceptionMessageContract.VOTING_MORE_THAN_ONE_VOTE_FOR_OPTION;
import static epamers.surwave.core.ExceptionMessageContract.VOTING_WRONG_SURVEY_STATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

import epamers.surwave.core.exceptions.ValidationException;
import epamers.surwave.dtos.VoteForm;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.User;
import epamers.surwave.entities.Vote;
import epamers.surwave.services.SurveyService;
import epamers.surwave.services.UserService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VoteListValidatorTest {

  private static final Long YOUR_OPTION_ID = 1L;
  private static final Long OTHER_OPTION_ID = 2L;
  private static final Long SURVEY_ID = 35L;
  private static final int VOTE_RATING = 1;
  private static final String CURRENT_USER_ID = "someGoogleId";
  private static final String OTHER_USER_ID = "anotherGoogleId";
  private static final Integer SURVEY_VOTES_BY_USER = 1;

  private VoteListValidator voteListValidator;

  @Mock
  private UserService userService;

  @Mock
  private SurveyService surveyService;

  private User currentUser;
  private Option otherOption;
  private Survey survey;
  private VoteForm voteForm;
  private List<VoteForm> voteForms;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    voteListValidator = new VoteListValidator(userService, surveyService);

    currentUser = User.builder()
        .id(CURRENT_USER_ID)
        .build();

    User otherUser = User.builder()
        .id(OTHER_USER_ID)
        .build();

    Option yourOption = Option.builder()
        .id(YOUR_OPTION_ID)
        .user(currentUser)
        .survey(survey)
        .votes(new HashSet<>())
        .build();

    otherOption = Option.builder()
        .id(OTHER_OPTION_ID)
        .user(otherUser)
        .survey(survey)
        .votes(new HashSet<>())
        .build();

    Set<Option> options = new HashSet<>();
    options.add(yourOption);
    options.add(otherOption);

    survey = ClassicSurvey.builder()
        .state(SurveyState.STARTED)
        .choicesByUser(SURVEY_VOTES_BY_USER)
        .id(SURVEY_ID)
        .options(options)
        .build();

    voteForm = VoteForm.builder()
        .optionId(OTHER_OPTION_ID)
        .surveyId(SURVEY_ID)
        .rating(VOTE_RATING)
        .build();

    voteForms = new ArrayList<>();
    voteForms.add(voteForm);

    when(surveyService.getById(SURVEY_ID)).thenReturn(survey);
    when(surveyService.getOptionById(YOUR_OPTION_ID)).thenReturn(yourOption);
    when(surveyService.getOptionById(OTHER_OPTION_ID)).thenReturn(otherOption);
    when(userService.getCurrent()).thenReturn(currentUser);
  }

  @Test
  public void addVotes_allValid_success() {
    voteListValidator.validate(voteForms);
  }

  @Test
  public void addVotes_notEnoughVotes_exception() {
    voteForms = new ArrayList<>();

    Throwable thrown = catchThrowable(() -> voteListValidator.validate(voteForms));

    assertThat(thrown).isInstanceOf(ValidationException.class)
        .hasMessage(VOTING_FOR_ZERO_OPTIONS);
  }

  @Test
  public void addVotes_twoVotesForOneOption_exception() {
    voteForms.add(voteForm);

    Throwable thrown = catchThrowable(() -> voteListValidator.validate(voteForms));

    assertThat(thrown).isInstanceOf(ValidationException.class)
        .hasMessage(VOTING_MORE_THAN_ONE_VOTE_FOR_OPTION);
  }

  @Test
  public void addVotes_votingForYourOption_exception() {
    voteForm.setOptionId(YOUR_OPTION_ID);

    Throwable thrown = catchThrowable(() -> voteListValidator.validate(voteForms));

    assertThat(thrown).isInstanceOf(ValidationException.class)
        .hasMessage(VOTING_FOR_YOUR_OPTION);
  }

  @Test
  public void addVotes_tooMuchVotes_exception() {
    VoteForm anotherForm = VoteForm.builder()
        .surveyId(SURVEY_ID)
        .rating(VOTE_RATING)
        .build();
    voteForms.add(anotherForm);
    String expectedMessage = String.format(VOTING_CHOICES_BY_USER_NOT_SATISFIED, SURVEY_VOTES_BY_USER, voteForms.size());

    Throwable thrown = catchThrowable(() -> voteListValidator.validate(voteForms));

    assertThat(thrown).isInstanceOf(ValidationException.class)
        .hasMessage(expectedMessage);
  }

  @Test
  public void addVotes_alreadyVoted_exception() {
    Vote vote2 = Vote.builder()
        .participant(currentUser)
        .build();
    otherOption.getVotes().add(vote2);

    Throwable thrown = catchThrowable(() -> voteListValidator.validate(voteForms));

    assertThat(thrown).isInstanceOf(ValidationException.class)
        .hasMessage(VOTING_ALREADY_VOTED);
  }

  @Test
  public void addVotes_invalidRating_exception() {
    voteForm.setRating(15);

    Throwable thrown = catchThrowable(() -> voteListValidator.validate(voteForms));

    assertThat(thrown).isInstanceOf(ValidationException.class)
        .hasMessage(VOTING_INVALID_RATING_FOR_CLASSIC_TYPE);
  }

  @Test
  public void addVotes_wrongSurveyState_exception() {
    survey.setState(SurveyState.CREATED);

    Throwable thrown = catchThrowable(() -> voteListValidator.validate(voteForms));

    assertThat(thrown).isInstanceOf(ValidationException.class)
        .hasMessage(VOTING_WRONG_SURVEY_STATE);
  }
}
