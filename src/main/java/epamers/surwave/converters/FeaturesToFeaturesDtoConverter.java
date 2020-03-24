package epamers.surwave.converters;

import epamers.surwave.dtos.FeaturesDto;
import epamers.surwave.entities.Features;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FeaturesToFeaturesDtoConverter implements Converter<Features, FeaturesDto> {

  @Override
  public FeaturesDto convert(Features features) {
    return FeaturesDto.builder()
        .danceability(features.getDanceability())
        .energy(features.getEnergy())
        .valence(features.getValence())
        .build();
  }
}
