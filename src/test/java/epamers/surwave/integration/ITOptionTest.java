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
import org.junit.Before;
import org.junit.Test;

//TODO: first version. Probably will require rewriting after changing Option entity. Also, need to add some 'Put' tests.
public class ITOptionTest extends IntegrationTest {

  private OptionForm optionForm;

  private final String AUTHOR = "Some Author";
  private final String MEDIA_URL = "http://youtube.com/supervideo256";
  private final String TITLE = "Elton John - Komarinskaya (feat. Ella Fitzgerald)";

  @Before
  public void setUp() {

    RestAssured.port = port;

    optionForm = OptionForm.builder()
        .author(AUTHOR)
        .mediaUrl(MEDIA_URL)
        .title(TITLE)
        .build();
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

    //Try to retrieve a newly created Option
    givenJson()
        .get(newEntityURI)
        .then()
        .statusCode(SC_OK)
        .body("title", equalTo(TITLE))
        .body("mediaUrl", equalTo(MEDIA_URL))
        .body("author", equalTo(AUTHOR));

    //Ensure that we have exactly one Option in repo
    givenJson()
        .get(OPTION_URL + "/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(1));

    //Then try to delete it
    givenJson()
        .delete(newEntityURI)
        .then()
        .statusCode(SC_OK);

    //Check that it's no longer exists
    givenJson()
        .get(newEntityURI)
        .then()
        .statusCode(SC_NOT_FOUND);

    //Check that we have zero Options in the end
    givenJson()
        .get(OPTION_URL + "/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(0));
  }
}