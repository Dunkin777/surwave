package epamers.surwave.services;

import static epamers.surwave.TestUtils.SONG_ID;
import static epamers.surwave.TestUtils.getValidSong;
import static epamers.surwave.core.ExceptionMessageContract.SONG_IS_NULL_CREATION;
import static epamers.surwave.core.ExceptionMessageContract.SONG_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import epamers.surwave.entities.Song;
import epamers.surwave.repos.SongRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

public class SongServiceTest {

  private static final Long SONG_NONEXISTENT_ID = 36L;

  @InjectMocks
  SongService songService;

  @Mock
  SongRepository songRepository;

  @Mock
  MediaFileService mediaFileService;

  @Mock
  MultipartFile multipartFile;

  private Song song;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    song = getValidSong();

    when(songRepository.existsById(SONG_ID)).thenReturn(true);
    when(songRepository.existsById(SONG_NONEXISTENT_ID)).thenReturn(false);
    when(songRepository.findById(SONG_ID)).thenReturn(Optional.of(song));
    when(songRepository.findAll()).thenReturn(List.of(song));
    when(songRepository.save(song)).thenReturn(song);
  }

  @Test
  public void getAll_success() {
    List<Song> songs = songService.getAll();

    assertThat(songs.size()).isEqualTo(1);
    assertThat(songs).contains(song);
  }

  @Test
  public void getById_existingId_success() {
    Song returnedSong = songService.getById(SONG_ID);

    assertThat(returnedSong).isEqualTo(song);
  }

  @Test
  public void getById_nonExistingId_exception() {
    Long nonexistantId = 13L;
    String expectedMessage = String.format(SONG_NOT_FOUND, nonexistantId);
    when(songRepository.existsById(SONG_ID)).thenReturn(false);

    Throwable thrown = catchThrowable(() -> songService.getById(nonexistantId));

    assertThat(thrown).isInstanceOf(EntityNotFoundException.class).hasMessage(expectedMessage);
  }

  @Test
  public void getOrCreate_newSong_created() {
    Song returnedSong = songService.getOrCreate(song, multipartFile);

    verify(songRepository).save(song);
    verify(mediaFileService).upload(multipartFile, song.getId());
    assertThat(returnedSong).isEqualTo(song);
  }

  @Test
  public void getOrCreate_existingSong_notCreated() {
    Song existingSong = getValidSong();
    when(songRepository.findByTitleIgnoreCaseAndPerformerIgnoreCase(any(), any())).thenReturn(Optional.of(existingSong));

    Song returnedSong = songService.getOrCreate(song, multipartFile);

    verify(songRepository, never()).save(any());
    assertThat(returnedSong).isEqualTo(existingSong);
  }

  @Test
  public void getOrCreate_nullSong_exception() {
    Throwable thrown = catchThrowable(() -> songService.getOrCreate(null, multipartFile));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
        .hasMessage(SONG_IS_NULL_CREATION);
  }
}
