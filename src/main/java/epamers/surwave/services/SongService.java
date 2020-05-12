package epamers.surwave.services;

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
    return songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Song with id " + id + " was not found in database."));
  }

  @Transactional
  public Song getOrCreate(Song song, MultipartFile mediaFile) {
    if (song == null) {
      throw new IllegalArgumentException("Got NULL song, cannot create.");
    }

    return songRepository.findByTitleIgnoreCaseAndPerformerIgnoreCase(song.getTitle(), song.getPerformer())
        .orElseGet(() -> {
          Song newSong = songRepository.save(song);

          String mediaPath = mediaFileService.upload(mediaFile, newSong.getId());
          newSong.setStorageKey(mediaPath);
          analyticsService.fillSongFeatures(newSong);

          return newSong;
        });
  }

  @Transactional
  public void update(Long id, Song song) {
    if (song == null) {
      throw new IllegalArgumentException("Got NULL song, cannot update.");
    }

    if (!songRepository.existsById(id)) {
      throw new EntityNotFoundException("Song with id " + id + " was not found in database.");
    }

    song.setId(id);
    songRepository.save(song);
  }
}
