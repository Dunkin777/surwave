package epamers.surwave.integration;

import static epamers.surwave.core.Contract.SONG_URL;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import epamers.surwave.dtos.SongForm;
import epamers.surwave.repos.SongRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ITSongTest extends IntegrationTest {

  @Autowired
  private SongRepository songRepository;

  private SongForm songForm;

  private final String PERFORMER = "Brian Wilson";
  private final String TITLE = "Komarinskaya (feat. Ella Fitzgerald)";
  private final String COMMENT = "Starts in D#, then sudden change to another religion.";

  @Before
  public void setUp() {
    RestAssured.port = port;

    songForm = SongForm.builder()
        .performer(PERFORMER)
        .title(TITLE)
        .comment(COMMENT)
        .build();
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

    //Post a new Song
    Response response = givenJson()
        .body(songForm)
        .post(SONG_URL)
        .then()
        .statusCode(SC_CREATED)
        .extract()
        .response();

    String newEntityURI = response.getHeader("Location");

    //Retrieve and check newly created Song
    givenJson()
        .get(newEntityURI)
        .then()
        .statusCode(SC_OK)
        .body("title", equalTo(TITLE))
        .body("comment", equalTo(COMMENT))
        .body("performer", equalTo(PERFORMER));

    //Ensure that we have exactly one Song in repo
    givenJson()
        .get(SONG_URL + "/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(1));

    //Change some property of created Song
    final String changedTitle = "Oh my!.. Title has changed & now it's even better!";
    songForm.setTitle(changedTitle);

    givenJson()
        .body(songForm)
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