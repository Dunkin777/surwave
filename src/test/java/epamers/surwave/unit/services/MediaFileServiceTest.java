package epamers.surwave.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.amazonaws.SdkClientException;
import epamers.surwave.core.exceptions.FileStorageException;
import epamers.surwave.services.MediaFileService;
import epamers.surwave.services.S3Service;
import java.io.IOException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

public class MediaFileServiceTest {

  private static final Long SONG_ID = 42L;

  @InjectMocks
  private MediaFileService mediaFileService;

  @Mock
  private S3Service s3Service;

  @Mock
  private MultipartFile multipartFile;

  @Captor
  private ArgumentCaptor<String> stringArgumentCaptor;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void upload_success() {
    String storageKey = "some_dir/" + SONG_ID + ".mp3";
    when(s3Service.putObject(stringArgumentCaptor.capture(), any(), any())).thenReturn(storageKey);

    String songS3Key = mediaFileService.upload(multipartFile, SONG_ID);

    assertThat(stringArgumentCaptor.getValue()).isEqualTo(SONG_ID + ".mp3");
    assertThat(songS3Key).isEqualTo(storageKey);
  }

  @Test
  public void upload_exceptionFromAws() {
    when(s3Service.putObject(any(), any(), any())).thenThrow(new SdkClientException("..."));

    assertThatThrownBy(() -> mediaFileService.upload(multipartFile, SONG_ID)).isInstanceOf(FileStorageException.class);
  }

  @Test
  public void upload_exceptionFromFile() throws IOException {
    when(multipartFile.getInputStream()).thenThrow(new IOException());

    assertThatThrownBy(() -> mediaFileService.upload(multipartFile, SONG_ID)).isInstanceOf(FileStorageException.class);
  }

  @Test
  public void getMediaPresignedUrl() {
    String mediaURL = RandomStringUtils.randomAlphabetic(5);
    when(s3Service.getPresignedURL(any())).thenReturn(mediaURL);

    String mediaPresignedUrl = mediaFileService.getMediaPresignedUrl(RandomStringUtils.randomAlphabetic(5));

    assertThat(mediaPresignedUrl).isEqualTo(mediaURL);
  }
}
