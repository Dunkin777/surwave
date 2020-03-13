package epamers.surwave.integration;

import static epamers.surwave.core.Contract.SONG_URL;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import com.jayway.restassured.RestAssured;
import epamers.surwave.dtos.SongForm;
import epamers.surwave.entities.Song;
import epamers.surwave.repos.SongRepository;
import epamers.surwave.repos.SurveyRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ITSongTest extends IntegrationTest {

  private final String SONG_PERFORMER = "Brian Wilson";
  private final String SONG_TITLE = "Komarinskaya (feat. Ella Fitzgerald)";

  @Autowired
  private SongRepository songRepository;

  @Autowired
  private SurveyRepository surveyRepository;

  private SongForm songForm;
  private Song song;

  @Before
  public void setUp() {
    RestAssured.port = port;

    songForm = SongForm.builder()
        .performer(SONG_PERFORMER)
        .title(SONG_TITLE)
        .build();

    song = Song.builder()
        .performer(SONG_PERFORMER)
        .mediaPath("/1.mp3")
        .title(SONG_TITLE)
        .build();
  }

  @After
  public void cleanUp() {
    songRepository.deleteAll();
    surveyRepository.deleteAll();
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

    //Ensure that we have exactly one Song in repo
    givenJson()
        .get(SONG_URL + "/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(1))
        .body("title", hasItem(SONG_TITLE))
        .body("performer", hasItem(SONG_PERFORMER));

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
        .get(SONG_URL + "/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(1))
        .body("title", hasItem(changedTitle));
  }
}