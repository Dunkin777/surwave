package epamers.surwave.integration;

import static epamers.surwave.core.Contract.SURVEY_URL;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import epamers.surwave.dtos.SongForm;
import epamers.surwave.dtos.SurveyForm;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.SurveyType;
import epamers.surwave.repos.SongRepository;
import epamers.surwave.repos.SurveyRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ITSurveyTest extends IntegrationTest {

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private SongRepository songRepository;

  private SongForm songForm;
  private SurveyForm surveyForm;

  private final String PERFORMER = "Elton John Lennon";
  private final String TITLE = "Korobeiniki (feat. George Gershwin)";
  private final String COMMENT = "Actually, I don't wanna to play this song, adding just for lulz...";
  private final String SURVEY_DESCRIPTION = "Please think twice before choosing!";

  @Before
  public void setUp() {
    RestAssured.port = port;

    songForm = SongForm.builder()
        .performer(PERFORMER)
        .title(TITLE)
        .comment(COMMENT)
        .build();

    surveyForm = SurveyForm.builder()
        .type(SurveyType.CLASSIC)
        .choicesByUser(5)
        .description(SURVEY_DESCRIPTION)
        .proposalsByUser(4)
        .isHidden(false)
        .build();
  }

  @After
  public void cleanUp() {
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

    //Retrieve and check newly created Survey
    givenJson()
        .get(newSurveyURI)
        .then()
        .statusCode(SC_OK)
        .body("description", equalTo(SURVEY_DESCRIPTION))
        .body("type", equalTo("CLASSIC"))
        .body("choicesByUser", equalTo(5))
        .body("proposalsByUser", equalTo(4))
        .body("state", equalTo("CREATED"))
        .body("isHidden", equalTo(false))
        .body("songs", hasSize(0));

    //Forcibly end this Survey
    surveyForm.setState(SurveyState.CLOSED);

    givenJson()
        .body(surveyForm)
        .put(newSurveyURI)
        .then()
        .statusCode(SC_OK);

    //Add a Song to our Survey
    response = givenJson()
        .body(songForm)
        .put(newSurveyURI + "/song")
        .then()
        .statusCode(SC_OK)
        .extract()
        .response();

    String newSongURI = response.getHeader("Location");

    //Check that all changes are saved successfully
    givenJson()
        .get(newSurveyURI)
        .then()
        .statusCode(SC_OK)
        .body("state", equalTo("CLOSED"))
        .body("songs", hasSize(1))
        .body("songs.performer", hasItem(PERFORMER))
        .body("songs.title", hasItem(TITLE))
        .body("songs.comment", hasItem(COMMENT));

    //Remove Song from Survey
    givenJson()
        .body(songForm)
        .delete(newSurveyURI + newSongURI)
        .then()
        .statusCode(SC_OK);

    //Check it
    givenJson()
        .get(newSurveyURI)
        .then()
        .statusCode(SC_OK)
        .body("songs", hasSize(0));

    assertTrue(songRepository.findAll().isEmpty());
  }
}