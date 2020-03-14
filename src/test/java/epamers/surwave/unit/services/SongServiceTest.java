package epamers.surwave.unit.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import epamers.surwave.entities.Song;
import epamers.surwave.repos.SongRepository;
import epamers.surwave.services.SongService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SongServiceTest {

  @InjectMocks
  SongService songService;

  @Mock
  SongRepository songRepository;

  private final Long SONG_ID = 156L;
  private final Long NONEXISTENT_ID = 36L;
  private final String PERFORMER = "Felix Mendelson";
  private final String TITLE = "Komarinskaya (feat. Ella Fitzgerald)";
  private Song song;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    song = Song.builder()
        .performer(PERFORMER)
        .title(TITLE)
        .id(SONG_ID)
        .build();

    when(songRepository.existsById(SONG_ID)).thenReturn(true);
    when(songRepository.existsById(NONEXISTENT_ID)).thenReturn(false);
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
  public void update_validCase_success() {
    songService.update(SONG_ID, song);

    verify(songRepository).save(song);
  }

  @Test(expected = NoSuchElementException.class)
  public void update_nonExistingId_exception() {
    songService.update(NONEXISTENT_ID, song);

    verify(songRepository, never()).save(song);
  }

  @Test(expected = IllegalArgumentException.class)
  public void update_nullSong_exception() {
    songService.update(SONG_ID, null);

    verify(songRepository, never()).save(song);
  }

  @Test
  public void delete_existingId_success() {
    songService.delete(SONG_ID);

    verify(songRepository).deleteById(SONG_ID);
  }

  @Test
  public void delete_nonExistingId_exception() {
    songService.delete(NONEXISTENT_ID);

    verify(songRepository, never()).deleteById(NONEXISTENT_ID);
  }
}