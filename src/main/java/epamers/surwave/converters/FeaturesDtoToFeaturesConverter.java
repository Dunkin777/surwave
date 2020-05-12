package epamers.surwave.converters;

import epamers.surwave.dtos.FeaturesDto;
import epamers.surwave.entities.Features;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FeaturesDtoToFeaturesConverter implements Converter<FeaturesDto, Features> {

  @Override
  public Features convert(FeaturesDto featuresDto) {
    return Features.builder()
        .valence(featuresDto.getValence())
        .energy(featuresDto.getEnergy())
        .danceability(featuresDto.getDanceability())
        .build();
  }
}
