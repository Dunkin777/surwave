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

  private final Long ID = 156L;
  private final Long NONEXISTENT_ID = 36L;
  private final String PERFORMER = "Felix Mendelson";
  private final String TITLE = "Komarinskaya (feat. Ella Fitzgerald)";
  private final String COMMENT = "Starts in D#, then sudden change to another religion.";
  private Song song;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    song = Song.builder()
        .performer(PERFORMER)
        .title(TITLE)
        .id(ID)
        .comment(COMMENT)
        .build();

    when(songRepository.existsById(ID)).thenReturn(true);
    when(songRepository.findById(ID)).thenReturn(Optional.of(song));
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
    Song returnedSong = songService.getById(ID);

    assertEquals(song, returnedSong);
  }

  @Test(expected = NoSuchElementException.class)
  public void getById_nonExistingId_exception() {
    when(songRepository.existsById(ID)).thenReturn(false);

    songService.getById(13L);
  }

  @Test
  public void create_validOption_success() {
    Song returnedSong = songService.create(song);

    verify(songRepository).save(song);
    assertEquals(song, returnedSong);
  }

  @Test(expected = IllegalArgumentException.class)
  public void create_nullArgument_exception() {
    songService.create(null);
  }

  @Test
  public void update_validCase_success() {
    songService.update(ID, song);

    verify(songRepository).save(song);
  }

  @Test(expected = NoSuchElementException.class)
  public void update_nonExistingId_exception() {
    songService.update(NONEXISTENT_ID, song);

    verify(songRepository, never()).save(song);
  }

  @Test(expected = IllegalArgumentException.class)
  public void update_nullOption_exception() {
    songService.update(ID, null);

    verify(songRepository, never()).save(song);
  }
}