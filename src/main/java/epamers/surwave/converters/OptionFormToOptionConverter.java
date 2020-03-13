package epamers.surwave.converters;

import epamers.surwave.dtos.OptionForm;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Song;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OptionFormToOptionConverter implements Converter<OptionForm, Option> {

  @Override
  public Option convert(OptionForm optionForm) {
    return Option.builder()
        .song(Song.builder()
            .id(optionForm.getSongId())
            .build())
        .comment(optionForm.getComment())
        .build();
  }
}
