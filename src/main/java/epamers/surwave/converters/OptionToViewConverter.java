package epamers.surwave.converters;

import epamers.surwave.dtos.OptionView;
import epamers.surwave.entities.Option;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OptionToViewConverter implements Converter<Option, OptionView> {

  @Override
  public OptionView convert(Option option) {

    return OptionView.builder()
        .author(option.getAuthor())
        .id(option.getId())
        .title(option.getTitle())
        .comment(option.getComment())
        .build();
  }
}
