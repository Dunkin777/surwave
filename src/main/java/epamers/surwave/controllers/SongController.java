package epamers.surwave.controllers;

import static epamers.surwave.core.Contract.SONG_URL;
import static epamers.surwave.core.Contract.UPLOAD_URL;

import epamers.surwave.dtos.SongForm;
import epamers.surwave.dtos.SongView;
import epamers.surwave.entities.Song;
import epamers.surwave.services.MediaUploadService;
import epamers.surwave.services.SongService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.stream.Collectors;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
      notes = "Returns a collection of SongViews. Basically, it's all Songs that ever were created "
          + "in our application."
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

  @PostMapping("/{id}" + UPLOAD_URL)
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(
      value = "Upload media to Song",
      notes = "Awaits Song ID as a path variable and file that you want to upload. File will be "
          + "saved for further examination and retrieval (e.g. for Survey page)."
  )
  public void uploadMediaToSong(@ApiParam(value = "Song ID") @PathVariable Long id, @RequestParam("file") MultipartFile file) {
    Song song = songService.getById(id);
    uploadService.upload(file, song.getTitle());
  }
}
