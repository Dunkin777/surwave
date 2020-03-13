package epamers.surwave.converters;

import epamers.surwave.dtos.OptionView;
import epamers.surwave.dtos.SongView;
import epamers.surwave.entities.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptionToOptionViewConverter implements Converter<Option, OptionView> {

  private final SongToSongViewConverter songToSongViewConverter;

  @Override
  public OptionView convert(Option option) {
    SongView song = songToSongViewConverter.convert(option.getSong());

    return OptionView.builder()
        .song(song)
        .comment(option.getComment())
        .build();
  }
}
