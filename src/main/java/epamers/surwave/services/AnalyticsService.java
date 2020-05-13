package epamers.surwave.services;

import static javax.transaction.Transactional.TxType.*;

import epamers.surwave.clients.AnalyticsClient;
import epamers.surwave.dtos.FeaturesDto;
import epamers.surwave.entities.Features;
import epamers.surwave.entities.Song;
import feign.FeignException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

  private final AnalyticsClient analyticsClient;
  private final ConversionService converter;

  @Async
  @Transactional(REQUIRES_NEW)
  public void fillSongFeatures(Song song) {
    FeaturesDto featuresDto;

    try {
      featuresDto = analyticsClient.getFeatures(song.getId(), song.getStorageKey());

    } catch (FeignException e) {
      log.error(String.valueOf(e.status()));
      log.error(e.contentUTF8());
      return;
    }

    Features features = converter.convert(featuresDto, Features.class);
    song.setFeatures(features);
  }
}
