package epamers.surwave.services;

import epamers.surwave.entities.Song;
import epamers.surwave.repos.SongRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class SongService {

  private final SongRepository songRepository;
  private final MediaUploadService mediaUploadService;

  public List<Song> getAll() {
    return songRepository.findAll();
  }

  public Song getById(Long id) {
    return songRepository.findById(id).orElseThrow();
  }

  @Transactional
  public Song create(Song song, MultipartFile mediaFile) {
    if (song == null) {
      throw new IllegalArgumentException();
    }

    song = songRepository.save(song);
    String mediaPath = mediaUploadService.upload(mediaFile, song.getId());
    song.setMediaPath(mediaPath);

    return song;
  }

  @Transactional
  public Song getOrCreate(Song song) {
    return songRepository.findByTitleIgnoreCaseAndPerformerIgnoreCase(song.getTitle(), song.getPerformer())
        .orElseGet(() -> songRepository.save(song));
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
