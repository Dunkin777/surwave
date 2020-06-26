package epamers.surwave.integration;

import static com.jayway.restassured.RestAssured.given;
import static epamers.surwave.core.Contract.SONG_URL;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import epamers.surwave.clients.AnalyticsClient;
import epamers.surwave.dtos.FeaturesDto;
import epamers.surwave.repos.SongRepository;
import epamers.surwave.services.S3Service;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ITSongTest extends IntegrationTest {

  private static final String S3_FILE_KEY = "somekey/someSong";
  private static final String SONG_PERFORMER = "Brian Wilson";
  private static final String SONG_TITLE = "Komarinskaya (feat. Ella Fitzgerald)";

  @MockBean
  private S3Service s3Service;

  @MockBean
  private AnalyticsClient analyticsClient;

  @Autowired
  private SongRepository songRepository;

  @Rule
  public final TemporaryFolder folder = new TemporaryFolder();

  @Before
  public void setUp() {
    RestAssured.port = port;
    FeaturesDto featuresDto = FeaturesDto.builder()
        .danceability(0.466)
        .energy(0.84)
        .valence(0.0)
        .build();

    when(s3Service.putObject(anyString(), any(), any())).thenReturn(S3_FILE_KEY);
    when(analyticsClient.getFeatures(any(), any())).thenReturn(featuresDto);
  }

  @After
  public void cleanUp() {
    songRepository.deleteAll();
  }

  @Test
  public void songController_successCase() throws IOException {
    //Check that we have zero Songs at the start
    givenJson()
        .get(SONG_URL + "/all")
        .then()
        .statusCode(SC_OK)
        .body("$", hasSize(0));

    //Create a new Song
    Response response = given()
        .with().auth().basic("guest", "guest")
        .multiPart("mediaFile", folder.newFile("song.mp3"))
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
  }

  @Test
  public void createSong_titleTooLong_error() {
    String tooLongTitle = RandomStringUtils.randomAlphabetic(101);

    given()
        .with().auth().basic("guest", "guest")
        .multiPart("mediaFile", new File("./readme.md"))
        .formParam("performer", SONG_PERFORMER)
        .formParam("title", tooLongTitle)
        .when()
        .post(SONG_URL)
        .then()
        .statusCode(SC_BAD_REQUEST);
  }
}
