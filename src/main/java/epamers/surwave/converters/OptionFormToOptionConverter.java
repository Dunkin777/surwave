package epamers.surwave.converters;

import epamers.surwave.dtos.OptionForm;
import epamers.surwave.dtos.SongForm;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Song;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OptionFormToOptionConverter implements Converter<OptionForm, Option> {

  @Override
  public Option convert(OptionForm optionForm) {
    Song song = Song.builder()
        .performer(optionForm.getPerformer())
        .title(optionForm.getTitle())
        .id(optionForm.getSongId())
        .build();

    return Option.builder()
        .song(song)
        .comment(optionForm.getComment())
        .build();
  }
}
