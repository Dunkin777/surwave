package epamers.surwave.converters;

import epamers.surwave.dtos.OptionForm;
import epamers.surwave.entities.Option;
import org.springframework.stereotype.Component;

@Component
public class FormToOptionConverter {

  public Option convert(OptionForm option) {

    return Option.builder()
        .author(option.getAuthor())
        .mediaUrl(option.getMediaUrl())
        .title(option.getTitle())
        .build();
  }
}
