package epamers.surwave.unit.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import epamers.surwave.entities.Song;
import epamers.surwave.repos.SongRepository;
import epamers.surwave.services.MediaFileService;
import epamers.surwave.services.SongService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

public class SongServiceTest {

  @InjectMocks
  SongService songService;

  @Mock
  SongRepository songRepository;

  @Mock
  MediaFileService mediaFileService;

  @Mock
  MultipartFile multipartFile;

  private static final Long SONG_ID = 156L;
  private static final Long SONG_NONEXISTENT_ID = 36L;
  private static final String SONG_PERFORMER = "Felix Mendelssohn";
  private static final String SONG_TITLE = "Komarinskaya (feat. Ella Fitzgerald)";
  private static final String SONG_MEDIA_PATH = "/data/1.mp3";
  private Song song;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    song = Song.builder()
        .performer(SONG_PERFORMER)
        .title(SONG_TITLE)
        .id(SONG_ID)
        .mediaPath(SONG_MEDIA_PATH)
        .build();

    when(songRepository.existsById(SONG_ID)).thenReturn(true);
    when(songRepository.existsById(SONG_NONEXISTENT_ID)).thenReturn(false);
    when(songRepository.findById(SONG_ID)).thenReturn(Optional.of(song));
    when(songRepository.findAll()).thenReturn(List.of(song));
    when(songRepository.save(song)).thenReturn(song);
  }

  @Test
  public void getAll_success() {
    List<Song> songs = songService.getAll();

    assertEquals(1, songs.size());
    assertTrue(songs.contains(song));
  }

  @Test
  public void getById_existingId_success() {
    Song returnedSong = songService.getById(SONG_ID);

    assertEquals(song, returnedSong);
  }

  @Test(expected = NoSuchElementException.class)
  public void getById_nonExistingId_exception() {
    when(songRepository.existsById(SONG_ID)).thenReturn(false);

    songService.getById(13L);
  }

  @Test
  public void getOrCreate_newSong_created() {
    Song returnedSong = songService.getOrCreate(song, multipartFile);

    verify(songRepository).save(song);
    verify(mediaFileService).upload(multipartFile, song.getId());
    assertEquals(song, returnedSong);
  }

  @Test
  public void getOrCreate_existingSong_notCreated() {
    Song existingSong = Song.builder().build();
    when(songRepository.findByTitleIgnoreCaseAndPerformerIgnoreCase(any(), any())).thenReturn(Optional.of(existingSong));

    Song returnedSong = songService.getOrCreate(song, multipartFile);

    verify(songRepository, never()).save(any());
    assertEquals(existingSong, returnedSong);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getOrCreate_nullSong_exception() {
    songService.getOrCreate(null, multipartFile);
  }

  @Test
  public void update_validCase_success() {
    songService.update(SONG_ID, song);

    verify(songRepository).save(song);
  }

  @Test(expected = NoSuchElementException.class)
  public void update_nonExistingId_exception() {
    songService.update(SONG_NONEXISTENT_ID, song);

    verify(songRepository, never()).save(song);
  }

  @Test(expected = IllegalArgumentException.class)
  public void update_nullSong_exception() {
    songService.update(SONG_ID, null);

    verify(songRepository, never()).save(song);
  }
}