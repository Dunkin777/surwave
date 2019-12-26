package epamers.surwave.integration;

import static epamers.surwave.core.Contract.OPTION_URL;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import epamers.surwave.dtos.OptionForm;
import epamers.surwave.repos.OptionRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ITOptionTest extends IntegrationTest {

  @Autowired
  private OptionRepository optionRepository;

  private OptionForm optionForm;

  private final String AUTHOR = "Some Author";
  private final String TITLE = "Elton John - Komarinskaya (feat. Ella Fitzgerald)";
  private final String COMMENT = "Starts in D#, then sudden change to another religion.";

  @Before
  public void setUp() {

    RestAssured.port = port;

    optionForm = OptionForm.builder()
        .author(AUTHOR)
        .title(TITLE)
        .comment(COMMENT)
        .build();
  }

  @After
  public void cleanUp() {
    optionRepository.deleteAll();
  }

  @Test
  public void optionController_successCase() {

    //Check that we have zero Options at the start
    givenJson()
        .get(OPTION_URL + "/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(0));

    //Post a new Option
    Response response = givenJson()
        .body(optionForm)
        .post(OPTION_URL)
        .then()
        .statusCode(SC_CREATED)
        .extract()
        .response();

    String newEntityURI = response.getHeader("Location");

    //Retrieve and check newly created Option
    givenJson()
        .get(newEntityURI)
        .then()
        .statusCode(SC_OK)
        .body("title", equalTo(TITLE))
        .body("comment", equalTo(COMMENT))
        .body("author", equalTo(AUTHOR));

    //Ensure that we have exactly one Option in repo
    givenJson()
        .get(OPTION_URL + "/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(1));

    //Change some property of created Option
    final String changedTitle = "Oh my!.. Title has changed & now it's even better!";
    optionForm.setTitle(changedTitle);

    givenJson()
        .body(optionForm)
        .put(newEntityURI)
        .then()
        .statusCode(SC_OK);

    //Check successfullness of the change
    givenJson()
        .get(newEntityURI)
        .then()
        .statusCode(SC_OK)
        .body("title", equalTo(changedTitle));
  }
}