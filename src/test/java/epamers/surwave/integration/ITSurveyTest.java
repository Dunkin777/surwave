package epamers.surwave.integration;

import static epamers.surwave.core.Contract.SURVEY_URL;
import static epamers.surwave.entities.SurveyState.CREATED;
import static epamers.surwave.entities.SurveyState.STOPPED;
import static epamers.surwave.entities.SurveyType.CLASSIC;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import epamers.surwave.dtos.SongForm;
import epamers.surwave.dtos.SurveyForm;
import epamers.surwave.repos.SongRepository;
import epamers.surwave.repos.SurveyRepository;
import epamers.surwave.repos.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ITSurveyTest extends SecurityTest {

  private static final String SONG_PERFORMER = "Elton John Lennon";
  private static final String SONG_TITLE = "Korobeiniki (feat. George Gershwin)";
  private static final String SURVEY_TITLE = "Sergei Yurzin Birthday's Songs";
  private static final String SURVEY_DESCRIPTION = "Please think twice before choosing!";

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private SongRepository songRepository;

  @Autowired
  private UserRepository userRepository;

  private SongForm songForm;
  private SurveyForm surveyForm;

  @Before
  public void setUp() {
    RestAssured.port = port;

    songForm = SongForm.builder()
        .performer(SONG_PERFORMER)
        .title(SONG_TITLE)
        .build();

    surveyForm = SurveyForm.builder()
        .type(CLASSIC)
        .choicesByUser(5)
        .description(SURVEY_DESCRIPTION)
        .proposalsByUser(4)
        .isHidden(false)
        .title(SURVEY_TITLE)
        .build();
  }

  @After
  public void cleanUp() {
    surveyRepository.deleteAll();
    userRepository.deleteAll();
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
        .body("choicesByUser", equalTo(5))
        .body("proposalsByUser", equalTo(4))
        .body("state", equalTo(CREATED.toString()))
        .body("isHidden", equalTo(false))
        .body("options", hasSize(0));

    //Forcibly end this Survey
    surveyForm.setState(STOPPED);

    givenJson()
        .body(surveyForm)
        .put(newSurveyURI)
        .then()
        .statusCode(SC_OK);

    givenJson()
        .get(newSurveyURI)
        .then()
        .statusCode(SC_OK)
        .body("state", equalTo(STOPPED.toString()));
  }
}
