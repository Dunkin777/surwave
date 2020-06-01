package epamers.surwave.controllers;

import static epamers.surwave.core.Contract.FEATURES_URL;
import static epamers.surwave.core.Contract.SONG_URL;

import epamers.surwave.dtos.SongForm;
import epamers.surwave.dtos.SongView;
import epamers.surwave.entities.Song;
import epamers.surwave.services.AnalyticsService;
import epamers.surwave.services.SongService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping(SONG_URL)
public class SongController {

  private final SongService songService;
  private final AnalyticsService analyticsService;
  private final ConversionService converter;

  @GetMapping("/all")
  @ApiOperation(
      value = "Get all songs",
      notes = "Returns a collection of SongViews, which will contain all Songs that were ever created in Surwave. No use for now on FE, in the "
          + "future will be probably transformed into autocompletion endpoint."
  )
  public List<SongView> getAll() {
    return songService.getAll().stream()
        .map(o -> converter.convert(o, SongView.class))
        .collect(Collectors.toList());
  }

  @PostMapping(consumes = {"multipart/form-data"})
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(
      value = "Create a new Song",
      notes = "Awaits SongForm as request body. File from it will be stored and processed in Surwave and can be retrieved later. "
          + "Returns new Song id in the Location header. Will use existing Song if finds one by title and performer."
  )
  public void create(@ModelAttribute @Valid SongForm songForm, @ApiIgnore HttpServletResponse response) {
    Song song = converter.convert(songForm, Song.class);
    Long songId = songService.getOrCreate(song, songForm.getMediaFile()).getId();

    response.addHeader("Location", SONG_URL + "/" + songId);
  }

  @PutMapping("/{songId}" + FEATURES_URL + "/recalculate")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(
      value = "Recalculate song features",
      notes = "Awaits song id as url argument. Asynchronously calls analytics service to rewrite song features info. "
          + "Body of request is ignored."
  )
  public void recalculateFeatures(@PathVariable Long songId) {
    analyticsService.fillSongFeatures(songId);
  }
}
