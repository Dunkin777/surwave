package epamers.surwave.clients;

import static epamers.surwave.core.Contract.FEATURES_URL;
import static epamers.surwave.core.Contract.SONG_URL;

import epamers.surwave.dtos.FeaturesDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface AnalyticsClient {

  @GetMapping(SONG_URL + "/{songId}" + FEATURES_URL)
  FeaturesDto getFeatures(@PathVariable Long songId, @RequestParam("song_key") String song_key);
}
