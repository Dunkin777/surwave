package epamers.surwave.converters;

import epamers.surwave.dtos.OptionForm;
import epamers.surwave.entities.Option;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FormToOptionConverter implements Converter<OptionForm, Option> {

  @Override
  public Option convert(OptionForm option) {

    return Option.builder()
        .author(option.getAuthor())
        .mediaUrl(option.getMediaUrl())
        .title(option.getTitle())
        .build();
  }
}
