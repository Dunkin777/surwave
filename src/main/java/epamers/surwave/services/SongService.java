package epamers.surwave.services;

import epamers.surwave.entities.Song;
import epamers.surwave.repos.SongRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class SongService {

  private final SongRepository songRepository;
  private final MediaFileService mediaFileService;

  public List<Song> getAll() {
    return songRepository.findAll();
  }

  public Song getById(Long id) {
    return songRepository.findById(id).orElseThrow();
  }

  @Transactional
  public Song getOrCreate(Song song, MultipartFile mediaFile) {
    if (song == null) {
      throw new IllegalArgumentException();
    }

    Optional<Song> dbSong = songRepository.findByTitleIgnoreCaseAndPerformerIgnoreCase(song.getTitle(), song.getPerformer());
    if(dbSong.isPresent()){
      return dbSong.get();
    }

    song = songRepository.save(song);
    String mediaPath = mediaFileService.upload(mediaFile, song.getId());
    song.setMediaPath(mediaPath);

    return song;
  }

  @Transactional
  public void update(Long id, Song song) {
    if (song == null) {
      throw new IllegalArgumentException();
    }

    if (!songRepository.existsById(id)) {
      throw new NoSuchElementException();
    }

    song.setId(id);
    songRepository.save(song);
  }

  @Transactional
  public void delete(Long id) {
    if (songRepository.existsById(id)) {
      songRepository.deleteById(id);
    }
  }
}
