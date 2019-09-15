package epamers.surwave.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

import com.jayway.restassured.RestAssured;
import epamers.surwave.dtos.OptionForm;
import org.junit.Before;
import org.junit.Test;

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
  public void controllerTest() {
    given()
        .contentType("application/json")
        .body(optionForm)
        .post("/option")
        .then()
        .statusCode(SC_OK);
  }
}