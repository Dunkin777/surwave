package epamers.surwave.configuration.cache;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CacheExpiryMap {

  @Value("${surwave.cache-ttl.media-url:60}")
  private Long mediaUrlDuration;

  public Map<String, Long> build() {
    final Map<String, Long> map = new HashMap<>();
    map.put(CacheConstants.MEDIA_URLS, Duration.ofMinutes(mediaUrlDuration).toMillis());

    return map;
  }
}
