package epamers.surwave.converters;

import epamers.surwave.dtos.FeaturesDto;
import epamers.surwave.dtos.SongView;
import epamers.surwave.entities.Song;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SongToSongViewConverter implements Converter<Song, SongView> {

  private final FeaturesToFeaturesDtoConverter featuresConverter;

  @Override
  public SongView convert(Song song) {
    FeaturesDto features = song.getFeatures() == null ? null : featuresConverter.convert(song.getFeatures());

    return SongView.builder()
        .performer(song.getPerformer())
        .id(song.getId())
        .title(song.getTitle())
        .mediaURL(song.getMediaURL())
        .features(features)
        .build();
  }
}
