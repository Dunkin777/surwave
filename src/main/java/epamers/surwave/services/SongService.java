package epamers.surwave.services;

import static epamers.surwave.core.ExceptionMessageContract.SONG_IS_NULL_CREATION;
import static epamers.surwave.core.ExceptionMessageContract.SONG_NOT_FOUND;

import epamers.surwave.entities.Song;
import epamers.surwave.repos.SongRepository;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class SongService {

  private final SongRepository songRepository;
  private final MediaFileService mediaFileService;
  private final AnalyticsService analyticsService;

  public List<Song> getAll() {
    return songRepository.findAll();
  }

  public Song getById(Long id) {
    return songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(SONG_NOT_FOUND, id)));
  }

  @Transactional
  public Song getOrCreate(Song song, MultipartFile mediaFile) {
    if (song == null) {
      throw new IllegalArgumentException(SONG_IS_NULL_CREATION);
    }

    return songRepository.findByTitleIgnoreCaseAndPerformerIgnoreCase(song.getTitle(), song.getPerformer())
        .orElseGet(() -> {
          Song newSong = songRepository.save(song);

          String mediaPath = mediaFileService.upload(mediaFile, newSong.getId());
          newSong.setStorageKey(mediaPath);
          analyticsService.fillSongFeatures(newSong.getId());

          return newSong;
        });
  }
}
