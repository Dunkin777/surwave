package epamers.surwave.configuration.cache;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CacheConstants {

  public final String NAMESPACE = "surwave";
  public final String KEY_DELIMITER = ".";
  public final String COMMON_KEY_GENERATOR = "commonKeyGenerator";

  /**
   * CacheNames
   */
  public final String MEDIA_URLS = "song_media_urls";
}
