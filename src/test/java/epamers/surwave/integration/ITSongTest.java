package epamers.surwave.integration;

import static epamers.surwave.core.Contract.SONG_URL;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.jayway.restassured.RestAssured;
import epamers.surwave.dtos.SongForm;
import epamers.surwave.entities.Song;
import epamers.surwave.repos.SongRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ITSongTest extends IntegrationTest {

  @Autowired
  private SongRepository songRepository;

  private SongForm songForm;
  private Song song;

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

    song = Song.builder()
        .comment(COMMENT)
        .performer(PERFORMER)
        .mediaPath("")
        .title(TITLE)
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

    //Create a new Song (somehow)
    Long newEntityId = songRepository.save(song).getId();
    String newEntityURI = SONG_URL + "/" + newEntityId;

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