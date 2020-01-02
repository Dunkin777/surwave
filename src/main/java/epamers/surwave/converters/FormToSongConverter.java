package epamers.surwave.converters;

import epamers.surwave.dtos.SongForm;
import epamers.surwave.entities.Song;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FormToSongConverter implements Converter<SongForm, Song> {

  @Override
  public Song convert(SongForm songForm) {
    return Song.builder()
        .performer(songForm.getPerformer())
        .title(songForm.getTitle())
        .comment(songForm.getComment())
        .surveyId(songForm.getSurveyId())
        .build();
  }
}
