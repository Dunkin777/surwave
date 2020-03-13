package epamers.surwave.controllers;

import static epamers.surwave.core.Contract.SONG_URL;

import epamers.surwave.dtos.SongForm;
import epamers.surwave.dtos.SongView;
import epamers.surwave.entities.Song;
import epamers.surwave.services.MediaUploadService;
import epamers.surwave.services.SongService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping(SONG_URL)
public class SongController {

  private final SongService songService;
  private final ConversionService converter;
  private final MediaUploadService uploadService;

  @GetMapping("/all")
  @ApiOperation(
      value = "Get all songs",
      notes = "Returns a collection of SongViews, which will contain all Songs that were ever "
          + "created in Surwave."
  )
  public List<SongView> getAllSongs() {
    return songService.getAll().stream()
        .map(o -> converter.convert(o, SongView.class))
        .collect(Collectors.toList());
  }

  @PutMapping("/{id}")
  @ApiOperation(
      value = "Update Song",
      notes = "Awaits Song ID as a path variable and SongForm as body. Allows to change data of "
          + "previously created Song (updated data will be available in all Surveys that are using "
          + "given Song)"
  )
  public void updateSong(@ApiParam(value = "Song ID") @PathVariable Long id,
      @ApiParam(value = "New Song data") @RequestBody @Valid SongForm songForm) {
    Song song = converter.convert(songForm, Song.class);
    songService.update(id, song);
  }

  @PostMapping(consumes = {"multipart/form-data"})
  @ApiOperation(
      value = "Creates a new Song",
      notes = "Awaits SongForm as request body. File from it will be "
          + "stored and processed in Surwave and can be retrieved later."
  )
  public void createSong(@ModelAttribute SongForm songForm, @ApiIgnore HttpServletResponse response) {
    Song song = converter.convert(songForm, Song.class);
    Long songId = songService.create(song, songForm.getMediaFile()).getId();

    response.addHeader("Location", SONG_URL + "/" + songId);
  }
}
