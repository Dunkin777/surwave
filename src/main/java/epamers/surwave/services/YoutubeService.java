package epamers.surwave.services;

import static epamers.surwave.core.ExceptionMessageContract.YOUTUBE_DL_ERROR;
import static epamers.surwave.core.PatternContract.MP3_EXTENSION;
import static epamers.surwave.core.PatternContract.YOUTUBE_LINK_PATTERN;

import epamers.surwave.core.exceptions.YouTubeException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeService {

  private static final String YOUTUBE_PREFIX = "https://www.youtube.com/watch?v=";
  private final Pattern pattern = Pattern.compile(YOUTUBE_LINK_PATTERN);

  @Value("${surwave.uploadDir:surwave_media}")
  private String uploadDirectory = "surwave_media";

  public String download(String youtubeLink, Long songId) {
    youtubeLink = prepareLink(youtubeLink);
    String path = String.format("%s/%d%s", uploadDirectory, songId, MP3_EXTENSION);
    String command = String.format("youtube-dl %s --audio-format mp3 -x --audio-quality 0 -o %s", youtubeLink, path);

    int exitCode = executeCommand(command);

    if (exitCode != 0) {
      throw new YouTubeException(YOUTUBE_DL_ERROR);
    }

    return path;
  }

  int executeCommand(String command) {
    try {
      Process process = Runtime.getRuntime().exec(command);
      return process.waitFor();
    } catch (IOException | InterruptedException e) {
      throw new YouTubeException(YOUTUBE_DL_ERROR, e);
    }
  }

  String prepareLink(String youtubeLink) {
    Matcher matcher = pattern.matcher(youtubeLink);
    if (matcher.find()) {
      String videoId = matcher.group(1);
      youtubeLink = YOUTUBE_PREFIX + videoId;
    }
    return youtubeLink;
  }
}
