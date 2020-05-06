package epamers.surwave.integration;

import static epamers.surwave.TestUtils.getValidClassicSurvey;
import static epamers.surwave.TestUtils.getValidUser;
import static epamers.surwave.core.Contract.OPTION_URL;
import static epamers.surwave.core.Contract.SURVEY_URL;
import static epamers.surwave.core.Contract.VOTE_URL;
import static epamers.surwave.entities.SurveyState.CREATED;
import static epamers.surwave.entities.SurveyState.STARTED;
import static epamers.surwave.entities.SurveyType.CLASSIC;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import epamers.surwave.dtos.OptionForm;
import epamers.surwave.dtos.SurveyForm;
import epamers.surwave.dtos.VoteForm;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Song;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.User;
import epamers.surwave.repos.OptionRepository;
import epamers.surwave.repos.SongRepository;
import epamers.surwave.repos.SurveyRepository;
import epamers.surwave.repos.UserRepository;
import epamers.surwave.repos.VoteRepository;
import epamers.surwave.services.S3Service;
import epamers.surwave.services.SurveyService;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ITSurveyTest extends IntegrationTest {

  private static final String SONG_PERFORMER = "Elton John Lennon";
  private static final String SONG_TITLE = "Korobeiniki (feat. George Gershwin)";
  private static final String SURVEY_TITLE = "Sergei Yurzin Birthday's Songs";
  private static final String SURVEY_DESCRIPTION = "Please think twice before choosing!";
  private static final String SURVEY_NEW_DESCRIPTION = "Now we have even cooler description! its super descriptive!";
  private static final String OPTION_COMMENT = "That's a real proper comment, totally believable.";

  @MockBean
  private S3Service s3Service;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private SongRepository songRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OptionRepository optionRepository;

  @Autowired
  private VoteRepository voteRepository;

  @Autowired
  private SurveyService surveyService;

  private SurveyForm surveyForm;
  private OptionForm optionForm;
  private Song anotherSong;

  @Before
  public void setUp() {
    RestAssured.port = port;

    surveyForm = SurveyForm.builder()
        .type(CLASSIC)
        .title(SURVEY_TITLE)
        .choicesByUser(1)
        .description(SURVEY_DESCRIPTION)
        .proposalsByUser(4)
        .isHidden(false)
        .build();

    Song song = Song.builder()
        .performer(SONG_PERFORMER)
        .title(SONG_TITLE)
        .storageKey("storageKey")
        .build();
    song = songRepository.save(song);

    anotherSong = Song.builder()
        .performer(SONG_PERFORMER)
        .title(SONG_TITLE)
        .storageKey("storageKey2")
        .build();
    anotherSong = songRepository.save(anotherSong);

    optionForm = OptionForm.builder()
        .songId(song.getId())
        .comment(OPTION_COMMENT)
        .build();

    Mockito.when(s3Service.getPresignedURL(any())).thenReturn("www.someurl.com/1.mp3");
  }

  @After
  public void cleanUp() {
    voteRepository.deleteAll();
    optionRepository.deleteAll();
    surveyRepository.deleteAll();
    songRepository.deleteAll();
  }

  @Test
  public void surveyController_successCase() {
    //Check that we have no Surveys at the start
    givenJson()
        .get(SURVEY_URL + "/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(0));

    //Create new blank Survey
    Response response = givenJson()
        .body(surveyForm)
        .post(SURVEY_URL)
        .then()
        .statusCode(SC_CREATED)
        .extract()
        .response();

    String newSurveyURI = response.getHeader("Location");
    String[] split = newSurveyURI.split("/");
    Long surveyId = Long.parseLong(split[2]);

    //Retrieve and check newly created Survey
    givenJson()
        .get(newSurveyURI)
        .then()
        .statusCode(SC_OK)
        .body("title", equalTo(SURVEY_TITLE))
        .body("description", equalTo(SURVEY_DESCRIPTION))
        .body("type", equalTo(CLASSIC.toString()))
        .body("choicesByUser", equalTo(1))
        .body("proposalsByUser", equalTo(4))
        .body("state", equalTo(CREATED.toString()))
        .body("isHidden", equalTo(false))
        .body("options", hasSize(0));

    surveyForm.setState(CREATED);

    //Update some data
    surveyForm.setDescription(SURVEY_NEW_DESCRIPTION);

    givenJson()
        .body(surveyForm)
        .put(newSurveyURI)
        .then()
        .statusCode(SC_OK);

    //Check that it was successful
    givenJson()
        .get(newSurveyURI)
        .then()
        .statusCode(SC_OK)
        .body("description", equalTo(SURVEY_NEW_DESCRIPTION));

    //Add new option
    response = givenJson()
        .body(optionForm)
        .post(SURVEY_URL + "/" + surveyId + OPTION_URL)
        .then()
        .statusCode(SC_CREATED)
        .extract()
        .response();

    String newOptionURI = response.getHeader("Location");
    split = newOptionURI.split("/");
    Long optionId = Long.parseLong(split[2]);

    //Check it
    givenJson()
        .get(newSurveyURI)
        .then()
        .statusCode(SC_OK)
        .body("options", hasSize(1))
        .body("options.comment", contains(OPTION_COMMENT))
        .body("options.song.title", contains(SONG_TITLE))
        .body("options.song.performer", contains(SONG_PERFORMER));

    //Let's create another user's option to vote for it
    Long newOptionId = createNewOption(surveyId);

    //Change Survey State
    surveyForm.setState(STARTED);

    givenJson()
        .body(surveyForm)
        .put(newSurveyURI)
        .then()
        .statusCode(SC_OK);

    //Check it
    givenJson()
        .get(newSurveyURI)
        .then()
        .statusCode(SC_OK)
        .body("state", equalTo(STARTED.toString()));

    //Vote
    VoteForm voteForm = VoteForm.builder()
        .optionId(newOptionId)
        .surveyId(surveyId)
        .rating(1)
        .build();
    List<VoteForm> votes = List.of(voteForm);

    givenJson()
        .body(votes)
        .put(newSurveyURI + VOTE_URL)
        .then()
        .statusCode(SC_OK);
  }

  private Long createNewOption(Long surveyId) {
    User anotherUser = getValidUser();
    userRepository.save(anotherUser);

    Option option = Option.builder()
        .song(Song.builder()
            .id(anotherSong.getId())
            .build())
        .comment("")
        .build();

    return surveyService.addOption(surveyId, option, anotherUser).getId();
  }

  private Long createNewSurvey() {
    Survey survey = getValidClassicSurvey();

    survey = surveyRepository.save(survey);

    return survey.getId();
  }

  @Test
  public void createSurvey_descriptionTooLong_error() {
    String descriptionOver300Chars = RandomStringUtils.randomAlphabetic(301);
    surveyForm.setDescription(descriptionOver300Chars);

    givenJson()
        .body(surveyForm)
        .post(SURVEY_URL)
        .then()
        .statusCode(SC_BAD_REQUEST);
  }

  @Test
  public void addOptionToSurvey_commentTooLong_error() {
    Long surveyId = createNewSurvey();

    String commentOver150Chars = RandomStringUtils.randomAlphabetic(151);
    optionForm.setComment(commentOver150Chars);

    givenJson()
        .body(optionForm)
        .post(SURVEY_URL + "/" + surveyId + OPTION_URL)
        .then()
        .statusCode(SC_BAD_REQUEST);
  }
}
