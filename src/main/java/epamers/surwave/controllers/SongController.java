package epamers.surwave.controllers;

import static epamers.surwave.core.Contract.SONG_URL;

import epamers.surwave.dtos.SongForm;
import epamers.surwave.dtos.SongView;
import epamers.surwave.entities.Song;
import epamers.surwave.services.SongService;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(SONG_URL)
public class SongController {

  private final SongService songService;
  private final ConversionService converter;

  @GetMapping("/all")
  public List<SongView> getAllSongs() {
    return songService.getAll().stream()
        .map(o -> converter.convert(o, SongView.class))
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public SongView getSong(@PathVariable Long id) {
    Song song = songService.getById(id);
    return converter.convert(song, SongView.class);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createSong(@RequestBody @Valid SongForm songForm, HttpServletResponse response) {
    Song song = songService.create(converter.convert(songForm, Song.class));
    response.addHeader("Location", SONG_URL + "/" + song.getId());
  }

  @PutMapping("/{id}")
  public void updateSong(@PathVariable Long id, @RequestBody @Valid SongForm songForm) {
    songService.update(id, converter.convert(songForm, Song.class));
  }
}
