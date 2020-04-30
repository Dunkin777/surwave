package epamers.surwave.integration;

import static com.jayway.restassured.RestAssured.given;
import static epamers.surwave.core.Contract.SONG_URL;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import epamers.surwave.repos.SongRepository;
import epamers.surwave.services.S3Service;
import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ITSongTest extends IntegrationTest {

  private static final String S3_FILE_KEY = "somekey/someSong";
  private static final String SONG_PERFORMER = "Brian Wilson";
  private static final String SONG_TITLE = "Komarinskaya (feat. Ella Fitzgerald)";

  @MockBean
  private S3Service s3Service;

  @Autowired
  private SongRepository songRepository;

  @Before
  public void setUp() {
    RestAssured.port = port;
    when(s3Service.putObject(anyString(), any(), any())).thenReturn(S3_FILE_KEY);
  }

  @After
  public void cleanUp() {
    songRepository.deleteAll();
  }

  @Test
  public void songController_successCase() {
    //Check that we have zero Songs at the start
    givenJson()
        .get(SONG_URL + "/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(0));

    //Create a new Song
    Response response = given()
        .with().auth().basic("guest", "guest")
        .multiPart("mediaFile", new File("./readme.md"))
        .formParam("performer", SONG_PERFORMER)
        .formParam("title", SONG_TITLE)
        .when()
        .post(SONG_URL)
        .then()
        .statusCode(SC_CREATED)
        .extract()
        .response();

    String newSongUri = response.getHeader("Location");
    Integer newSongId = Integer.valueOf(newSongUri.substring(newSongUri.lastIndexOf("/") + 1));

    //Ensure that we have exactly one Song in repo
    givenJson()
        .get(SONG_URL + "/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(1))
        .body("id", hasItem(newSongId))
        .body("mediaPath", not(isEmptyOrNullString()))
        .body("title", hasItem(SONG_TITLE))
        .body("performer", hasItem(SONG_PERFORMER));

    //Try to add already existing Song
    given()
        .with().auth().basic("guest", "guest")
        .multiPart("mediaFile", new File("./readme.md"))
        .formParam("performer", SONG_PERFORMER)
        .formParam("title", SONG_TITLE)
        .when()
        .post(SONG_URL)
        .then()
        .statusCode(SC_CREATED);

    //Check that it has returned the same location
    assertEquals(newSongUri, response.getHeader("Location"));

    //Check that we still have one Song
    givenJson()
        .get(SONG_URL + "/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(1));
  }
}
