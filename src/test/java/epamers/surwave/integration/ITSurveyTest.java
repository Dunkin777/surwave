package epamers.surwave.integration;

import static epamers.surwave.core.Contract.SURVEY_URL;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import epamers.surwave.dtos.SurveyForm;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.SurveyType;
import epamers.surwave.repos.OptionRepository;
import epamers.surwave.repos.SurveyRepository;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ITSurveyTest extends IntegrationTest {

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private OptionRepository optionRepository;

  private Option createdOption;
  private SurveyForm surveyForm;

  private final String AUTHOR = "Some Author";
  private final String MEDIA_URL = "http://youtube.com/supervideo256";
  private final String TITLE = "Elton John - Komarinskaya (feat. Ella Fitzgerald)";
  private final String COMMENT = "Starts in D#, then sudden change to another religion.";

  private final String SURVEY_DESCRIPTION = "Please think twice before choosing!";

  @Before
  public void setUp() {

    RestAssured.port = port;

    Option option = Option.builder()
        .author(AUTHOR)
        .mediaUrl(MEDIA_URL)
        .title(TITLE)
        .comment(COMMENT)
        .build();

    surveyForm = SurveyForm.builder()
        .type(SurveyType.CLASSIC)
        .choicesByUser(5)
        .description(SURVEY_DESCRIPTION)
        .isUsersSeparated(true)
        .proposalsByUser(4)
        .build();

    createdOption = optionRepository.save(option);
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

    String newEntityURI = response.getHeader("Location");

    //Retrieve and check newly created Survey
    givenJson()
        .get(newEntityURI)
        .then()
        .statusCode(SC_OK)
        .body("description", equalTo(SURVEY_DESCRIPTION))
        .body("type", equalTo("CLASSIC"))
        .body("choicesByUser", equalTo(5))
        .body("proposalsByUser", equalTo(4))
        .body("isUsersSeparated", equalTo(true))
        .body("state", equalTo("CREATED"))
        .body("options", hasSize(0));

    //Add an Option to our Survey
    surveyForm.setOptionIds(List.of(createdOption.getId()));
    surveyForm.setState(SurveyState.CREATED);

    givenJson()
        .body(surveyForm)
        .put(newEntityURI)
        .then()
        .statusCode(SC_OK);
  }
}