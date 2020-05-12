package epamers.surwave;

import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Features;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Role;
import epamers.surwave.entities.Song;
import epamers.surwave.entities.Survey;
import epamers.surwave.entities.SurveyState;
import epamers.surwave.entities.User;
import java.util.HashSet;

public class TestUtils {

  public static final String USER_ID = "someGoogleId";
  public static final String USER_NAME = "Velimir Mstislavovich";
  public static final String USER_EMAIL = "velya@home.su";

  public static final String SONG_PERFORMER = "Elton John Lennon";
  public static final String SONG_TITLE = "Komarinskaya (feat. Ella Fitzgerald)";
  public static final String SONG_STORAGE_KEY = "/data/1.mp3";
  public static final String SONG_MEDIA_URL = "www.amazom.com/1.mp3";
  public static final Long SONG_ID = 156L;

  public static final Long OPTION_ID = 1L;

  public static final String SURVEY_TITLE = "Sergei Yurzin Birthday's Songs";
  public static final String SURVEY_DESCRIPTION = "Please think twice before choosing!";
  public static final Long SURVEY_ID = 35L;
  public static final Integer SURVEY_PROPOSALS_BY_USER = 5;
  public static final Integer SURVEY_CHOICES_BY_USER = 1;

  public static User getValidUser() {
    User user = User.builder()
        .id(USER_ID)
        .username(USER_NAME)
        .email(USER_EMAIL)
        .locale("GB/en")
        .avatarUrl("www.com")
        .active(true)
        .build();

    user.addRole(Role.USER);

    return user;
  }

  public static Features getValidFeatures() {
    return Features.builder()
        .danceability(0.6)
        .energy(0.59)
        .valence(0.9999)
        .build();
  }

  public static Song getValidSong() {
    return Song.builder()
        .performer(SONG_PERFORMER)
        .title(SONG_TITLE)
        .storageKey(SONG_STORAGE_KEY)
        .id(SONG_ID)
        .features(getValidFeatures())
        .build();
  }

  public static Option getValidOption() {
    return Option.builder()
        .id(OPTION_ID)
        .user(getValidUser())
        .song(getValidSong())
        .survey(getValidClassicSurvey())
        .votes(new HashSet<>())
        .build();
  }

  public static Survey getValidClassicSurvey() {
    return ClassicSurvey.builder()
        .state(SurveyState.CREATED)
        .title(SURVEY_TITLE)
        .choicesByUser(SURVEY_CHOICES_BY_USER)
        .description(SURVEY_DESCRIPTION)
        .id(SURVEY_ID)
        .proposalsByUser(SURVEY_PROPOSALS_BY_USER)
        .options(new HashSet<>())
        .isHidden(false)
        .build();
  }
}
