package epamers.surwave.services;

import static epamers.surwave.core.ExceptionMessageContract.YOUTUBE_DL_ERROR;
import static epamers.surwave.core.PatternContract.MP3_EXTENSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import epamers.surwave.core.exceptions.YouTubeException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class YoutubeServiceTest {

  private static final String YOUTUBE_LINK = "https://www.youtube.com/watch?v=2Ff0PGN1ID8";
  private static final String PLAYLIST_LINK = "https://www.youtube.com/watch?v=2Ff0PGN1ID8&list=PLz1dDV5m0YHkrF9HUYndX2F4_xIsA6jRf";
  private static final Long SONG_ID = 1L;

  @Spy
  private YoutubeService youtubeService = new YoutubeService();

  @Test
  public void download_linkIsValid_songDownloaded() {
    doReturn(0).when(youtubeService).executeCommand(anyString());
    doReturn(YOUTUBE_LINK).when(youtubeService).prepareLink(YOUTUBE_LINK);

    String actualPath = youtubeService.download(YOUTUBE_LINK, SONG_ID);

    String expectedPath = String.format("%s/%d%s", "surwave_media", SONG_ID, MP3_EXTENSION);

    assertThat(actualPath).isEqualTo(expectedPath);
    verify(youtubeService).prepareLink(YOUTUBE_LINK);
    verify(youtubeService).executeCommand(anyString());
  }

  @Test
  public void download_processReturnedNotZero_exceptionThrown() {
    doReturn(1).when(youtubeService).executeCommand(anyString());
    doReturn(YOUTUBE_LINK).when(youtubeService).prepareLink(YOUTUBE_LINK);

    Throwable throwable = catchThrowable(() -> {
      youtubeService.download(YOUTUBE_LINK, SONG_ID);
    });

    assertThat(throwable).isInstanceOf(YouTubeException.class).hasMessage(YOUTUBE_DL_ERROR);
  }

  @Test
  public void prepareLink_linkFromPlayList_validLinkReturned() {
    String actualLink = youtubeService.prepareLink(PLAYLIST_LINK);

    assertThat(actualLink).isEqualTo(YOUTUBE_LINK);
  }
}
