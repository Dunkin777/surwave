package epamers.surwave.unit.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import epamers.surwave.controllers.SongController;
import epamers.surwave.dtos.SongForm;
import epamers.surwave.dtos.SongView;
import epamers.surwave.entities.Song;
import epamers.surwave.services.SongService;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

public class SongControllerTest {

  @InjectMocks
  SongController songController;

  @Mock
  SongService songService;

  @Mock
  ConversionService converter;

  @Mock
  HttpServletResponse response;

  private final Long ID = 156L;
  private final String PERFORMER = "Some Author";
  private final String TITLE = "Elton John - Komarinskaya (feat. Ella Fitzgerald)";
  private final String COMMENT = "Starts in D#, then sudden change to another religion.";

  private Song song;
  private SongView songView;
  private SongForm songForm;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    song = Song.builder()
        .performer(PERFORMER)
        .title(TITLE)
        .id(ID)
        .comment(COMMENT)
        .build();

    songForm = SongForm.builder().build();
    songView = SongView.builder().build();

    when(songService.getAll()).thenReturn(List.of(song));
    when(songService.getById(ID)).thenReturn(song);
    when(songService.create(song)).thenReturn(song);
    when(converter.convert(song, SongView.class)).thenReturn(songView);
    when(converter.convert(songForm, Song.class)).thenReturn(song);
  }

  @Test
  public void getAllOptions_success() {
    List<SongView> returnedOptions = songController.getAllOptions();

    assertEquals(1, returnedOptions.size());
    assertTrue(returnedOptions.contains(songView));
  }

  @Test
  public void getOption_existingId_success() {
    SongView returnedOption = songController.getOption(ID);

    assertEquals(songView, returnedOption);
  }

  @Test
  public void createOption() {
    songController.createOption(songForm, response);

    verify(songService).create(song);
    verify(response).addHeader(any(), any());
  }

  @Test
  public void updateOption() {
    songController.updateOption(ID, songForm);

    verify(converter).convert(songForm, Song.class);
    verify(songService).update(ID, song);
  }
}