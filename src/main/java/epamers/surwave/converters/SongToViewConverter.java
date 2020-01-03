package epamers.surwave.converters;

import epamers.surwave.dtos.SongView;
import epamers.surwave.entities.Song;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SongToViewConverter implements Converter<Song, SongView> {

  @Override
  public SongView convert(Song song) {
    return SongView.builder()
        .performer(song.getPerformer())
        .id(song.getId())
        .title(song.getTitle())
        .comment(song.getComment())
        .mediaPath(song.getMediaPath())
        .build();
  }
}
