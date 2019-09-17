package epamers.surwave.integration;

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

//TODO: first version. Probably will require rewriting after changing Option entity. Also, need to add 'Put' tests.
public class ITOptionTest extends IntegrationTest {

  private OptionForm optionForm;

  @Before
  public void setUp() {

    RestAssured.port = port;

    optionForm = OptionForm.builder()
        .author("ME")
        .mediaUrl("hahaha.com")
        .title("hahaha")
        .build();
  }

  @Test
  public void optionControllerTest() {

    //Check that we have zero Options at the start
    givenJson()
        .get("/option/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(0));

    //Post a new Option
    Response response = givenJson()
        .body(optionForm)
        .post("/option")
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
        .body("title", equalTo("hahaha"))
        .body("mediaUrl", equalTo("hahaha.com"))
        .body("author", equalTo("ME"));

    //Ensure that we have exactly one Option in repo
    givenJson()
        .get("/option/all")
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
        .get("/option/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(0));
  }
}