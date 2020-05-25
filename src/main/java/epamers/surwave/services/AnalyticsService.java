package epamers.surwave.services;

import epamers.surwave.clients.AnalyticsClient;
import epamers.surwave.dtos.FeaturesDto;
import epamers.surwave.entities.Features;
import epamers.surwave.entities.Song;
import epamers.surwave.repos.SongRepository;
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
  private final SongRepository songRepository;

  @Async
  @Transactional
  public void fillSongFeatures(Long songId) {
    FeaturesDto featuresDto;
    Song song = songRepository.findById(songId).orElseThrow();

    try {
      featuresDto = analyticsClient.getFeatures(songId, song.getStorageKey());

      Features features = converter.convert(featuresDto, Features.class);
      song.setFeatures(features);
    } catch (FeignException e) {
      log.error(String.valueOf(e.status()));
      log.error(e.contentUTF8());
    }
  }
}