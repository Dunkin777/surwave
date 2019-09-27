package epamers.surwave.converters;

import epamers.surwave.dtos.OptionForm;
import epamers.surwave.entities.Option;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FormToOptionConverter implements Converter<OptionForm, Option> {

  @Override
  public Option convert(OptionForm optionForm) {

    return Option.builder()
        .author(optionForm.getAuthor())
        .mediaUrl(optionForm.getMediaUrl())
        .title(optionForm.getTitle())
        .comment(optionForm.getComment())
        .build();
  }
}
