package epamers.surwave.validators;

import static epamers.surwave.core.ExceptionMessageContract.INCORRECT_YOUTUBE_LINK_FORMAT;
import static epamers.surwave.core.ExceptionMessageContract.NO_SOURCE;
import static epamers.surwave.core.ExceptionMessageContract.OBJECT_IS_NULL;
import static epamers.surwave.core.ExceptionMessageContract.TOO_MANY_SOURCES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;

import epamers.surwave.core.exceptions.ValidationException;
import epamers.surwave.dtos.SongForm;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

public class SongFormValidatorTest {

  private SurwaveValidator<SongForm> validator;

  @Before
  public void setUp() {
    validator = new SongFormValidator();
  }

  @Test
  public void validate_formIsNull_exceptionThrown() {
    Throwable throwable = catchThrowable(() -> validator.validate(null));

    assertThat(throwable).isInstanceOf(ValidationException.class).hasMessage(OBJECT_IS_NULL);
  }

  @Test
  public void validate_mediafileNullAndYoutubeLinkBlank_exceptionThrown() {
    SongForm songForm = new SongForm();

    Throwable throwable = catchThrowable(() -> validator.validate(songForm));

    assertThat(throwable).isInstanceOf(ValidationException.class).hasMessage(NO_SOURCE);
  }

  @Test
  public void validate_youtubbeLinkHasIncorrectPattern_exceptionThrown() {
    SongForm songForm = SongForm.builder()
        .sourceLink("https://www.baeldung.com")
        .build();

    Throwable throwable = catchThrowable(() -> validator.validate(songForm));

    assertThat(throwable).isInstanceOf(ValidationException.class).hasMessage(String.format(INCORRECT_YOUTUBE_LINK_FORMAT, songForm.getSourceLink()));
  }

  @Test
  public void validate_mediafileNotNullAndYoutubeLinkNotBlank_exceptionThrown() {
    MultipartFile mediafile = mock(MultipartFile.class);
    SongForm songForm = SongForm.builder()
        .mediaFile(mediafile)
        .sourceLink("https://www.youtube.com/watch?v=TLV4_xaYynY&list=PLMKA5kzkfqk2GEImRCIqGqWmQvKYygUhG")
        .build();

    Throwable throwable = catchThrowable(() -> validator.validate(songForm));

    assertThat(throwable).isInstanceOf(ValidationException.class).hasMessage(TOO_MANY_SOURCES);
  }
}
