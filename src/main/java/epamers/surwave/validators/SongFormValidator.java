package epamers.surwave.validators;

import static epamers.surwave.core.ExceptionMessageContract.INCORRECT_YOUTUBE_LINK_FORMAT;
import static epamers.surwave.core.ExceptionMessageContract.NO_SOURCE;
import static epamers.surwave.core.ExceptionMessageContract.OBJECT_IS_NULL;
import static epamers.surwave.core.ExceptionMessageContract.TOO_MANY_SOURCES;
import static epamers.surwave.core.PatternContract.YOUTUBE_LINK_PATTERN;

import epamers.surwave.core.exceptions.ValidationException;
import epamers.surwave.dtos.SongForm;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.utils.StringUtils;

@Component
public class SongFormValidator implements SurwaveValidator<SongForm> {

  private final Pattern pattern = Pattern.compile(YOUTUBE_LINK_PATTERN);

  @Override
  public void validate(SongForm entity) {

    if (Objects.isNull(entity)) {
      throw new ValidationException(OBJECT_IS_NULL);
    }

    String youtubeLink = entity.getSourceLink();
    if (Objects.isNull(entity.getMediaFile()) && StringUtils.isBlank(youtubeLink)) {
      throw new ValidationException(NO_SOURCE);
    }

    if (!Objects.isNull(entity.getMediaFile()) && StringUtils.isNotBlank(youtubeLink)) {
      throw new ValidationException(TOO_MANY_SOURCES);
    }

    if (StringUtils.isNotBlank(youtubeLink)) {

      Matcher matcher = pattern.matcher(entity.getSourceLink());

      if (!matcher.find()) {
        throw new ValidationException(String.format(INCORRECT_YOUTUBE_LINK_FORMAT, youtubeLink));
      }
    }
  }
}
