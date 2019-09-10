package epamers.surwave.converters;

import epamers.surwave.dtos.OptionView;
import epamers.surwave.entities.Option;
import org.springframework.stereotype.Component;

@Component
public class OptionToViewConverter {

  public OptionView convert(Option option) {

    return OptionView.builder()
        .author(option.getAuthor())
        .id(option.getId())
        .mediaUrl(option.getMediaUrl())
        .title(option.getTitle())
        .build();
  }
}
