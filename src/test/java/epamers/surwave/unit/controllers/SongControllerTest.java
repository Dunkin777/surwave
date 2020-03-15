package epamers.surwave.unit.controllers;

import static epamers.surwave.core.Contract.SONG_URL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import epamers.surwave.controllers.SongController;
import epamers.surwave.dtos.SongForm;
import epamers.surwave.dtos.SongView;
import epamers.surwave.entities.Song;
import epamers.surwave.services.MediaFileService;
import epamers.surwave.services.SongService;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.multipart.MultipartFile;

public class SongControllerTest {

  @InjectMocks
  SongController songController;

  @Mock
  SongService songService;

  @Mock
  MediaFileService uploadService;

  @Mock
  ConversionService converter;

  @Mock
  HttpServletResponse response;

  @Mock
  MultipartFile multipartFile;

  private static final Long SONG_ID = 156L;
  private static final String PERFORMER = "Some Author";
  private static final String TITLE = "Komarinskaya (feat. Ella Fitzgerald)";

  private Song song;
  private SongView songView;
  private SongForm songForm;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    song = Song.builder()
        .performer(PERFORMER)
        .title(TITLE)
        .id(SONG_ID)
        .build();

    songForm = SongForm.builder()
        .mediaFile(multipartFile)
        .build();
    songView = SongView.builder().build();

    when(songService.getAll()).thenReturn(List.of(song));
    when(converter.convert(song, SongView.class)).thenReturn(songView);
    when(converter.convert(songForm, Song.class)).thenReturn(song);
  }

  @Test
  public void getAll_success() {
    List<SongView> returnedSongs = songController.getAll();

    assertEquals(1, returnedSongs.size());
    verify(converter).convert(song, SongView.class);
    assertTrue(returnedSongs.contains(songView));
  }

  @Test
  public void create_success() {
    final Long newSongId = 5L;
    Song createdSong = Song.builder()
        .id(newSongId)
        .build();

    when(songService.getOrCreate(eq(song), any())).thenReturn(createdSong);

    songController.create(songForm, response);

    verify(converter).convert(songForm, Song.class);
    verify(songService).getOrCreate(song, multipartFile);
    verify(response).addHeader("Location", SONG_URL + "/" + newSongId);
  }
}